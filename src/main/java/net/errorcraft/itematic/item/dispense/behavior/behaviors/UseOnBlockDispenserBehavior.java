package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;

public class UseOnBlockDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        StackReference stackReference = StackReferenceUtil.of(stack);
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        ActionContext actionContext = ActionContext.builder(pointer.world(), stack, stackReference::set)
            .position(ActionContextParameter.THIS, pointer.pos())
            .position(ActionContextParameter.TARGET, pointer.pos().offset(direction))
            .side(direction)
            .build();
        if (!stack.itematic$invokeEvent(ItemEvents.USE_ON_BLOCK, actionContext)) {
            this.setSuccess(false);
        }
        return stackReference.get();
    }
}
