package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DispensableItemComponent;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DispenserBlock.class)
public class DispenserBlockExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public DispenserBehavior getBehaviorForItem(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.DISPENSABLE)
            .map(DispensableItemComponent::behavior)
            .map(RegistryEntry::value)
            .orElse(DispenseBehaviors.ITEM);
    }
}
