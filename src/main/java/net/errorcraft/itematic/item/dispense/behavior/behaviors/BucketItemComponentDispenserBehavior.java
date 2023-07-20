package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BucketItemComponent;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class BucketItemComponentDispenserBehavior extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Optional<BucketItemComponent> optionalItemComponent = stack.getComponent(ItemComponentTypes.BUCKET);
        if (optionalItemComponent.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }

        Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
        BlockPos position = pointer.getPos().offset(direction);
        BlockHitResult hitResult = new BlockHitResult(position.toCenterPos(), direction, position, true);
        TypedActionResult<ItemStack> result = optionalItemComponent.get().place(pointer.getWorld(), null, Hand.MAIN_HAND, stack, hitResult);
        if (!result.getResult().isAccepted()) {
            return super.dispenseSilently(pointer, stack);
        }
        ItemStack newStack = result.getValue();
        if (newStack.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }

        stack.decrement(1);
        if (stack.isEmpty()) {
            return newStack;
        }
        if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(newStack) < 0) {
            super.dispenseSilently(pointer, newStack);
        }
        return stack;
    }
}
