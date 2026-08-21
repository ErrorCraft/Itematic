package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public record DropItemFromBlockAction(PositionTarget position, ItemStack item) implements Action<DropItemFromBlockAction> {
    public static final MapCodec<DropItemFromBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(DropItemFromBlockAction::position),
        ItemStack.CODEC.fieldOf("item").forGetter(DropItemFromBlockAction::item)
    ).apply(instance, DropItemFromBlockAction::new));

    public static DropItemFromBlockAction of(PositionTarget position, Holder<Item> item) {
        return new DropItemFromBlockAction(position, new ItemStack(item));
    }

    @Override
    public ActionType<DropItemFromBlockAction> type() {
        return ActionType.DROP_ITEM_FROM_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Direction side = context.get(ItematicContextKeys.SIDE);
        if (side == null) {
            return false;
        }

        Block.popResourceFromFace(context.level(), pos, side, this.item.copy());
        return true;
    }
}
