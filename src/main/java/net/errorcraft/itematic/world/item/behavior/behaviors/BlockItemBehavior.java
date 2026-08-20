package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.placement.block.BlockPlacer;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Set;

public record BlockItemBehavior(BlockPicker<?> block, boolean operatorOnly, Set<Pass> passes) implements ItemBehavior<BlockItemBehavior> {
    public static final Codec<BlockItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(BlockItemBehavior::block),
        Codec.BOOL.optionalFieldOf("operator_only", false).forGetter(BlockItemBehavior::operatorOnly),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(BlockItemBehavior::passes)
    ).apply(instance, BlockItemBehavior::new));

    public static BlockItemBehavior of(BlockPicker<?> block, boolean operatorOnly, Set<Pass> passes) {
        return new BlockItemBehavior(block, operatorOnly, passes);
    }

    public static BlockItemBehavior of(Holder<Block> block) {
        return of(new SimpleBlockPicker(block), false, Pass.DEFAULT_PASSES);
    }

    public static BlockItemBehavior of(Holder<Block> block, Pass... passes) {
        return of(new SimpleBlockPicker(block), false, Set.of(passes));
    }

    public static BlockItemBehavior operator(Holder<Block> block) {
        return of(new SimpleBlockPicker(block), true, Pass.DEFAULT_PASSES);
    }

    public static BlockItemBehavior attachedToSide(Holder<Block> attachedBlock, Holder<Block> otherBlock, Direction attachedSide) {
        return of(new AttachedToSideBlockPicker(attachedBlock, otherBlock, attachedSide), false, Pass.DEFAULT_PASSES);
    }

    @Override
    public ItemBehaviorType<BlockItemBehavior> type() {
        return ItemBehaviorType.BLOCK;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ItemResult.PASS;
        }

        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(level, user, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        UseOnContext context = new UseOnContext(level, user, hand, stack, blockHitResult);
        return this.place(context, stackExchanger);
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        return this.place(context, stackExchanger);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        this.block.defaultBlock().value().itematic$addComponents(builder);
    }

    public boolean canBeNested() {
        return !(this.block.defaultBlock().value() instanceof ShulkerBoxBlock);
    }

    public void onDestroyed(ItemEntity item) {
        ItemContainerContents container = item.getItem().set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        if (container != null) {
            ItemUtils.onContainerDestroyed(item, container.nonEmptyItemsCopy());
        }
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    public boolean place(ActionContext context, PositionTarget position, boolean decrementCount) {
        BlockPlacer placer = BlockPlacer.of(
            context,
            position,
            this.block,
            this.operatorOnly,
            null
        );
        if (!placer.place()) {
            return false;
        }

        if (decrementCount) {
            context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY)
                .consume(
                    1,
                    context.get(LootContextParams.THIS_ENTITY, LivingEntity.class)
                );
        }

        return true;
    }

    private ItemResult place(UseOnContext context, ItemStackExchanger stackExchanger) {
        ActionContext actionContext = new BlockPlaceContext(context)
            .itematic$actionContext(stackExchanger);
        if (this.place(actionContext, PositionTarget.INTERACTED, true)) {
            return ItemResult.SUCCEED;
        }

        return ItemResult.PASS;
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
