package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DispensableItemComponent;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
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
    public DispenseItemBehavior getDispenseMethod(Level world, ItemStack stack) {
        return behavior(stack).orElse(DispenseBehaviors.FALLBACK);
    }

    @Unique
    private static Optional<DispenseItemBehavior> behavior(ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.DISPENSABLE)
            .map(DispensableItemComponent::behavior)
            .map(Holder::value);
    }
}
