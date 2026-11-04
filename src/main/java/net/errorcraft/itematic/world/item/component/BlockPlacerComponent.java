package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.placement.block.BlockPlacer;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

public record BlockPlacerComponent(BlockPicker<?> block, boolean operatorOnly, Set<Pass> passes) {
    public static final Codec<BlockPlacerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(BlockPlacerComponent::block),
        Codec.BOOL.optionalFieldOf("operator_only", false).forGetter(BlockPlacerComponent::operatorOnly),
        Pass.SET_CODEC.optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(BlockPlacerComponent::passes)
    ).apply(instance, BlockPlacerComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockPlacerComponent> STREAM_CODEC = StreamCodec.composite(
        BlockPicker.STREAM_CODEC, BlockPlacerComponent::block,
        ByteBufCodecs.BOOL, BlockPlacerComponent::operatorOnly,
        Pass.SET_STREAM_CODEC, BlockPlacerComponent::passes,
        BlockPlacerComponent::new
    );

    public boolean canUse(Pass pass) {
        return this.passes.contains(pass);
    }

    public InteractionResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(level, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        UseOnContext context = new UseOnContext(level, user, hand, stack, blockHitResult);
        return this.useOn(context, stackExchanger);
    }

    public InteractionResult useOn(UseOnContext context, ItemStackExchanger stackExchanger) {
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPlacer placer = BlockPlacer.of(placeContext, stackExchanger, this.block, this.operatorOnly, null);
        if (placer.place()) {
            context.getItemInHand().consume(1, context.getPlayer());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    public void addComponents(DataComponentMap.Builder builder) {
        this.block.defaultBlock().value().itematic$addComponents(builder);
    }

    public boolean canFitInsideContainerItems() {
        return !(this.block.defaultBlock().value() instanceof ShulkerBoxBlock);
    }

    public void onDestroyed(ItemEntity item) {
        ItemContainerContents container = item.getItem().set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        if (container != null) {
            ItemUtils.onContainerDestroyed(item, container.nonEmptyItemCopyStream());
        }
    }

    public boolean place(ActionContext context, PositionTarget position, boolean decrementCount) {
        BlockPlacer placer = BlockPlacer.of(context, position, this.block, this.operatorOnly, null);
        if (placer == null || !placer.place()) {
            return false;
        }

        if (decrementCount) {
            context.getOrDefault(LootContextParams.TOOL, ItemStack.class, ItemStack.EMPTY)
                .consume(
                    1,
                    context.get(LootContextParams.THIS_ENTITY, LivingEntity.class)
                );
        }

        return true;
    }

    public enum Pass implements StringRepresentable {
        BLOCK(0, "block"),
        FLUID(1, "fluid");

        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringRepresentable.fromEnum(Pass::values);
        public static final Codec<Set<Pass>> SET_CODEC = SetCodec.forEnum(CODEC);
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
