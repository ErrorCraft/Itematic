package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public record DropItemFromBlockAction(PositionTarget position, ItemStack item) implements Action<DropItemFromBlockAction> {
    public static final MapCodec<DropItemFromBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(DropItemFromBlockAction::position),
        ItemStack.CODEC.fieldOf("item").forGetter(DropItemFromBlockAction::item)
    ).apply(instance, DropItemFromBlockAction::new));

    public static DropItemFromBlockAction of(PositionTarget position, RegistryEntry<Item> item) {
        return new DropItemFromBlockAction(position, new ItemStack(item));
    }

    @Override
    public ActionType<DropItemFromBlockAction> type() {
        return ActionTypes.DROP_ITEM_FROM_BLOCK;
    }

    @Override
    public boolean execute(NewActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        if (side == null) {
            return false;
        }

        Block.dropStack(context.world(), pos, side, this.item.copy());
        return true;
    }
}
