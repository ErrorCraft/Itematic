package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.world.action.actions.FertilizeAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;

public class FertilizeDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction side = pointer.state().get(DispenserBlock.FACING);
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionContext context = ActionContext.builder(pointer.world(), stack, stackReference::set)
            .position(ActionContextParameter.TARGET, pointer.pos().offset(side))
            .side(side)
            .build();
        if (FertilizeAction.INSTANCE.execute(context)) {
            return stackReference.get();
        }
        this.setSuccess(false);
        return stack;
    }
}
