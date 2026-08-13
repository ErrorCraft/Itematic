package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
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
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public DispenseItemBehavior getDispenseMethod(Level world, ItemStack stack) {
        return behavior(stack).orElse(DispenseBehaviors.FALLBACK);
    }

    @Unique
    private static Optional<DispenseItemBehavior> behavior(ItemStack stack) {
        return stack.itematic$getBehavior(ItemBehaviorType.DISPENSABLE)
            .map(DispensableItemBehavior::behavior)
            .map(Holder::value);
    }
}
