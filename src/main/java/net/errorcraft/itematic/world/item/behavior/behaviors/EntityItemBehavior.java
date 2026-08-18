package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.mixin.world.item.HangingEntityItemAccessor;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.DiscardEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionEntitySpawnRule;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public record EntityItemBehavior(EntitySpawner entity, boolean allowSpawnerModification, Set<Pass> passes) implements ItemBehavior<EntityItemBehavior> {
    public static final Codec<EntityItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawner.CODEC.fieldOf("entity").forGetter(EntityItemBehavior::entity),
        Codec.BOOL.optionalFieldOf("allow_spawner_modification", false).forGetter(EntityItemBehavior::allowSpawnerModification),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityItemBehavior::passes)
    ).apply(instance, EntityItemBehavior::new));
    private static final Component RANDOM_VARIANT_TOOLTIP = HangingEntityItemAccessor.randomVariantTooltip();

    public static EntityItemBehavior of(Holder<EntityType<?>> entity) {
        return new EntityItemBehavior(
            EntitySpawner.of(entity),
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemBehavior of(EntitySpawner entity) {
        return new EntityItemBehavior(
            entity,
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemBehavior of(EntitySpawner entity, boolean allowSpawnerModification) {
        return new EntityItemBehavior(
            entity,
            allowSpawnerModification,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemBehavior of(EntitySpawner entity, boolean allowSpawnerModification, Pass... passes) {
        return new EntityItemBehavior(
            entity,
            allowSpawnerModification,
            Set.of(passes)
        );
    }

    public static ItemBehavior<?>[] ofDispensing(Holder<EntityType<?>> entity, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return ofDispensing(EntitySpawner.of(entity), dispenseBehaviors);
    }

    public static ItemBehavior<?>[] ofDispensing(EntitySpawner entity, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            of(entity),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
        };
    }

    public static ItemBehavior<?>[] minecart(Holder<EntityType<?>> entity, HolderGetter<Block> blocks, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return ofDispensing(
            EntitySpawner.builder(entity)
                .spawnRule(
                    DiscardEntitySpawnRule.INSTANCE,
                    InvertedLootItemCondition.invert(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .of(blocks, BlockTags.RAILS))
                        )
                    ))
                .spawnRule(OffsetSpawnPositionEntitySpawnRule.of(new Vec3(0.0d, 0.0625d, 0.0d)))
                .spawnRule(
                    OffsetSpawnPositionEntitySpawnRule.of(new Vec3(0.0d, 0.5d, 0.0d)),
                    LocationCheckPredicates.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.location()
                            .setBlock(BlockPredicate.Builder.block()
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .itematic$range(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST, RailShape.ASCENDING_SOUTH)))
                    ))
                .build(),
            dispenseBehaviors
        );
    }

    public static ItemBehavior<?>[] spawnEgg(Holder<EntityType<?>> entity, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            of(
                EntitySpawner.builder(entity)
                    .allowItemData()
                    .build(),
                true,
                EntityItemBehavior.Pass.BLOCK,
                EntityItemBehavior.Pass.FLUID
            ),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)),
            SpawnEggItemBehavior.INSTANCE
        };
    }

    @Override
    public ItemBehaviorType<EntityItemBehavior> type() {
        return ItemBehaviorType.ENTITY;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ItemResult.PASS;
        }

        if (world.isClientSide()) {
            return ItemResult.SUCCEED;
        }

        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        UseOnContext itemUsageContext = new UseOnContext(world, user, hand, stack, blockHitResult);
        this.modifyOrPlace(itemUsageContext, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        if (context.getLevel().isClientSide()) {
            return ItemResult.SUCCEED;
        }

        this.modifyOrPlace(context, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (this.entity.entity().value() != EntityType.PAINTING) {
            return;
        }

        Holder<PaintingVariant> paintingVariant = stack.get(DataComponents.PAINTING_VARIANT);
        if (paintingVariant != null) {
            paintingVariant.value().title().ifPresent(builder);
            paintingVariant.value().author().ifPresent(builder);
            builder.accept(Component.translatable("painting.dimensions", paintingVariant.value().width(), paintingVariant.value().height()));
        } else if (tooltipFlag.isCreative()) {
            builder.accept(RANDOM_VARIANT_TOOLTIP);
        }
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private void modifyOrPlace(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (!this.tryModifyOrPlace(context, stackExchanger)) {
            return;
        }

        context.getItemInHand().consume(
            1,
            context.getPlayer()
        );
    }

    private boolean tryModifyOrPlace(UseOnContext context, ItemStackExchanger stackExchanger) {
        Level world = context.getLevel();
        if (world.isClientSide()) {
            return false;
        }

        if (this.modifySpawner(context)) {
            return true;
        }

        ActionContext actionContext = ActionContext.builder(world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, context.getPlayer())
            .addOptional(LootContextParams.ORIGIN, context.getPlayer(), Entity::position)
            .add(ItematicContextParameters.INTERACTED_POSITION, context.getClickedPos().getCenter())
            .add(LootContextParams.TOOL, context.getItemInHand())
            .add(ItematicContextParameters.HAND, context.getHand())
            .add(ItematicContextParameters.SIDE, context.getClickedFace())
            .build();
        return this.place(actionContext, PositionTarget.INTERACTED) != null;
    }

    private boolean modifySpawner(UseOnContext context) {
        if (!this.allowSpawnerModification) {
            return false;
        }

        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        if (!state.is(Blocks.SPAWNER)) {
            return false;
        }

        Optional<SpawnerBlockEntity> blockEntity = world.getBlockEntity(pos, BlockEntityType.MOB_SPAWNER);
        if (blockEntity.isEmpty()) {
            return false;
        }

        this.modifySpawner(context, world, blockEntity.get(), pos, state);
        return true;
    }

    private void modifySpawner(UseOnContext context, Level world, SpawnerBlockEntity blockEntity, BlockPos pos, BlockState state) {
        EntityType<?> type = this.entity.entityType(context.getItemInHand());
        blockEntity.setEntityId(type, world.getRandom());
        blockEntity.setChanged();
        world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
        world.gameEvent(
            context.getPlayer(),
            GameEvent.BLOCK_CHANGE,
            pos
        );
    }

    public Entity place(ActionContext context, PositionTarget position) {
        return EntityPlacer.of(this.entity, null)
            .place(context, position, EntitySpawnReason.SPAWN_ITEM_USE);
    }

    public enum Pass implements StringRepresentable {
        BLOCK("block"),
        FLUID("fluid");

        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringRepresentable.fromEnum(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
