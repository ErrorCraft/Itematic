package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DispensableItemComponent;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(DispenserBlock.class)
public class DispenserBlockExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public DispenserBehavior getBehaviorForItem(World world, ItemStack stack) {
        return behavior(stack).orElse(DispenseBehaviors.FALLBACK);
    }

    @Unique
    private static Optional<DispenserBehavior> behavior(ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.DISPENSABLE)
            .map(DispensableItemComponent::behavior)
            .map(RegistryEntry::value);
    }
}
