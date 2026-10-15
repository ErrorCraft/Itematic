package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.mixin.world.item.CushionItemAccessor;
import net.errorcraft.itematic.mixin.world.item.HangingEntityItemAccessor;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.placement.EntityPlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public record EntityPlacerComponent(EntitySpawner entity, Optional<HolderSet<Block>> usesCollisionShape, boolean allowSpawnerModification, Set<Pass> passes) implements TooltipProvider {
    public static final Codec<EntityPlacerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawner.CODEC.fieldOf("entity").forGetter(EntityPlacerComponent::entity),
        RegistryCodecs.holderSet(Registries.BLOCK).optionalFieldOf("uses_collision_shape").forGetter(EntityPlacerComponent::usesCollisionShape),
        Codec.BOOL.optionalFieldOf("allow_spawner_modification", false).forGetter(EntityPlacerComponent::allowSpawnerModification),
        Pass.SET_CODEC.optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityPlacerComponent::passes)
    ).apply(instance, EntityPlacerComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityPlacerComponent> STREAM_CODEC = StreamCodec.composite(
        EntitySpawner.STREAM_CODEC, EntityPlacerComponent::entity,
        ByteBufCodecs.holderSet(Registries.BLOCK).apply(ByteBufCodecs::optional), EntityPlacerComponent::usesCollisionShape,
        ByteBufCodecs.BOOL, EntityPlacerComponent::allowSpawnerModification,
        Pass.SET_STREAM_CODEC, EntityPlacerComponent::passes,
        EntityPlacerComponent::new
    );
    public static final ScopedValue<HolderSet<Block>> USES_COLLISION_SHAPE = ScopedValue.newInstance();
    private static final Component RANDOM_VARIANT_TOOLTIP = HangingEntityItemAccessor.randomVariantTooltip();

    public static EntityPlacerComponent of(EntitySpawner entity) {
        return new EntityPlacerComponent(
            entity,
            Optional.empty(),
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityPlacerComponent of(EntitySpawner entity, HolderSet<Block> usesCollisionShape) {
        return new EntityPlacerComponent(
            entity,
            Optional.of(usesCollisionShape),
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityPlacerComponent of(EntitySpawner entity, boolean allowSpawnerModification, Pass... passes) {
        return new EntityPlacerComponent(
            entity,
            Optional.empty(),
            allowSpawnerModification,
            Set.of(passes)
        );
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.entity.entity().value() != EntityTypes.PAINTING) {
            return;
        }

        Holder<PaintingVariant> paintingVariant = components.get(DataComponents.PAINTING_VARIANT);
        if (paintingVariant != null) {
            paintingVariant.value().title().ifPresent(consumer);
            paintingVariant.value().author().ifPresent(consumer);
            consumer.accept(Component.translatable("painting.dimensions", paintingVariant.value().width(), paintingVariant.value().height()));
        } else if (flag.isCreative()) {
            consumer.accept(RANDOM_VARIANT_TOOLTIP);
        }
    }

    public boolean canUse(Pass pass) {
        return this.passes.contains(pass);
    }

    public InteractionResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(level, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        UseOnContext itemUsageContext = new UseOnContext(level, user, hand, stack, blockHitResult);
        return this.useOn(itemUsageContext, stackExchanger);
    }

    public InteractionResult useOn(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        UseOnContext recalculatedContext = this.recalculateContext(context);
        if (!this.tryModifyOrPlace(recalculatedContext, stackExchanger)) {
            return InteractionResult.FAIL;
        }

        context.getItemInHand().consume(1, context.getPlayer());
        return InteractionResult.SUCCESS;
    }

    @Nullable
    public Entity place(ActionContext context, PositionTarget position, @Nullable Double exactY) {
        return EntityPlacer.of(this.entity, null)
            .place(context, position, exactY, EntitySpawnReason.SPAWN_ITEM_USE);
    }

    private UseOnContext recalculateContext(UseOnContext context) {
        return this.usesCollisionShape.map(
            usesCollisionShape -> ScopedValue.where(USES_COLLISION_SHAPE, usesCollisionShape)
                .call(() -> CushionItemAccessor.recalculateContextForSpecialCollisionShapes(context))
        ).orElse(context);
    }

    private boolean tryModifyOrPlace(UseOnContext context, ItemStackExchanger stackExchanger) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return false;
        }

        if (this.modifySpawner(context)) {
            return true;
        }

        ActionContext actionContext = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, context.getPlayer())
            .addOptional(LootContextParams.ORIGIN, context.getPlayer(), Entity::position)
            .add(ItematicContextKeys.INTERACTED_POSITION, Vec3.atCenterOf(context.getClickedPos()))
            .add(LootContextParams.TOOL, context.getItemInHand())
            .add(ItematicContextKeys.HAND, context.getHand())
            .add(ItematicContextKeys.SIDE, context.getClickedFace())
            .build();
        return this.place(actionContext, PositionTarget.INTERACTED, context.getClickLocation().y()) != null;
    }

    private boolean modifySpawner(UseOnContext context) {
        if (!this.allowSpawnerModification) {
            return false;
        }

        BlockPos pos = context.getClickedPos();
        if (!(context.getLevel() instanceof ServerLevel level)) {
            return false;
        }

        Optional<SpawnerBlockEntity> blockEntity = level.getBlockEntity(pos, BlockEntityTypes.MOB_SPAWNER);
        if (blockEntity.isEmpty()) {
            return false;
        }

        if (level.isSpawnerBlockEnabled()) {
            this.modifySpawner(context, level, blockEntity.get(), pos);
            return true;
        }

        if (context.getPlayer() instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.translatable("advMode.notEnabled.spawner"));
        }

        return false;
    }

    private void modifySpawner(UseOnContext context, Level level, SpawnerBlockEntity blockEntity, BlockPos pos) {
        EntityType<?> type = this.entity.entityType(context.getItemInHand());
        blockEntity.setEntityId(type, level.getRandom());
        blockEntity.setChanged();
        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
        level.gameEvent(
            context.getPlayer(),
            GameEvent.BLOCK_CHANGE,
            pos
        );
    }

    public enum Pass implements StringRepresentable {
        BLOCK(0, "block"),
        FLUID(1, "fluid");

        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringRepresentable.fromEnum(Pass::values);
        public static final Codec<Set<Pass>> SET_CODEC = SetCodec.forEnum(Pass.CODEC);
        private static final IntFunction<Pass> BY_ID = ByIdMap.continuous(
            Pass::id,
            values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );
        public static final StreamCodec<ByteBuf, Pass> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Pass::id);
        public static final StreamCodec<ByteBuf, Set<Pass>> SET_STREAM_CODEC = ByteBufCodecs.collection(HashSet::new, STREAM_CODEC);

        private final int id;
        private final String name;

        Pass(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int id() {
            return this.id;
        }
    }
}
