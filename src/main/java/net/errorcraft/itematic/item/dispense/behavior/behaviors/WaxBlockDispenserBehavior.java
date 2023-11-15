package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.world.action.actions.WaxBlockAction;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;

public class WaxBlockDispenserBehavior extends FallibleItemDispenserBehavior {
    private static final WaxBlockAction ACTION = new WaxBlockAction(ActionContextParameter.TARGET);

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction side = pointer.state().get(DispenserBlock.FACING);
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionContext context = MutableActionContext.stackUsage(pointer.world(), stack, stackReference::set)
            .position(ActionContextParameter.TARGET, pointer.pos().offset(side))
            .side(side);
        if (ACTION.execute(context)) {
            return getAndDecrement(stackReference);
        }
        this.setSuccess(false);
        return stack;
    }

    private static ItemStack getAndDecrement(StackReference stackReference) {
        ItemStack stack = stackReference.get();
        stack.decrement(1);
        return stack;
    }
}
