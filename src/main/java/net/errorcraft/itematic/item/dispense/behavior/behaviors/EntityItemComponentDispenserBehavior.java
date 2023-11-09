package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EntityItemComponent;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;

import java.util.Optional;

public class EntityItemComponentDispenserBehavior extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        Optional<EntityItemComponent> optionalItemComponent = stack.itematic$getComponent(ItemComponentTypes.ENTITY);
        if (optionalItemComponent.isEmpty()) {
            return super.dispenseSilently(pointer, stack);
        }
        try {
            EntityItemComponent itemComponent = optionalItemComponent.get();
            StackReference stackReference = StackReferenceUtil.of(stack);
            EntityPlacer entityPlacer = EntityPlacer.dispensed(pointer, stack, stackReference::set, itemComponent);
            entityPlacer.place();
            return stackReference.get();
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing entity from dispenser at {}", pointer.pos(), exception);
            return ItemStack.EMPTY;
        }
    }
}
