package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.*;

import java.util.Optional;

@Mixin(DispenserBlock.class)
public class DispenserBlockExtender {
    @Shadow
    @Final
    private static DefaultDispenseItemBehavior DEFAULT_BEHAVIOR;

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public DispenseItemBehavior getDispenseMethod(Level level, ItemStack stack) {
        return behavior(stack).orElse(DEFAULT_BEHAVIOR);
    }

    @Unique
    private static Optional<DispenseItemBehavior> behavior(ItemStack stack) {
        return stack.itematic$getBehavior(ItemBehaviorType.DISPENSABLE)
            .map(DispensableItemBehavior::behavior)
            .map(Holder::value);
    }
}
