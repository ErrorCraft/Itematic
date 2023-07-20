package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.world.action.actions.FertilizeAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;

import java.util.Optional;

public class FertilizeDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction side = pointer.getBlockState().get(DispenserBlock.FACING);
        BlockPos position = pointer.getPos().offset(side);
        ActionContext context = new ActionContext(pointer.getWorld(), Optional.empty(), position.toCenterPos(), side, stack);
        this.fertilize(context, position);
        return stack;
    }

    private void fertilize(ActionContext context, BlockPos position) {
        if (FertilizeAction.INSTANCE.execute(context)) {
            context.world().syncWorldEvent(WorldEvents.BONE_MEAL_USED, position, 0);
            return;
        }
        this.setSuccess(false);
    }
}
