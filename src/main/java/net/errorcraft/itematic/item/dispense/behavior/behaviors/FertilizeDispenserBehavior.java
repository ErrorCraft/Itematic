package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.world.action.actions.FertilizeAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;

public class FertilizeDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction side = pointer.state().get(DispenserBlock.FACING);
        BlockPos position = pointer.pos().offset(side);
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionContext context = MutableActionContext.stackUsage(pointer.world(), stack, stackReference::set)
            .position(ActionContextParameter.THIS, position)
            .side(side);
        this.fertilize(context, position);
        return stackReference.get();
    }

    private void fertilize(ActionContext context, BlockPos position) {
        if (FertilizeAction.INSTANCE.execute(context)) {
            context.world().syncWorldEvent(WorldEvents.BONE_MEAL_USED, position, 0);
            return;
        }
        this.setSuccess(false);
    }
}
