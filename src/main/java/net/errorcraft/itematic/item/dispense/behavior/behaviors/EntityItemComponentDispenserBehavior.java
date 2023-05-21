package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EntityItemComponent;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class EntityItemComponentDispenserBehavior extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
        Optional<EntityItemComponent> optionalItemComponent = stack.getComponent(ItemComponentTypes.ENTITY);
        if (optionalItemComponent.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }
        try {
            EntityType<?> entityType = optionalItemComponent.get().getEntityType(stack);
            EntityPlacer entityPlacer = EntityPlacer.dispensed(pointer, stack, entityType, direction);
            entityPlacer.place();
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing entity from dispenser at {}", pointer.getPos(), exception);
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
