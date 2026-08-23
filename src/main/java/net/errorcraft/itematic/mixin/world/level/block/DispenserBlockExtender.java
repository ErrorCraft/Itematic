package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(DispenserBlock.class)
public class DispenserBlockExtender {
    @Shadow
    @Final
    private static DefaultDispenseItemBehavior DEFAULT_BEHAVIOR;

    @WrapMethod(
        method = "getDispenseMethod"
    )
    public DispenseItemBehavior useItemBehavior(Level level, ItemStack itemStack, Operation<DispenseItemBehavior> original) {
        return behavior(itemStack).orElse(DEFAULT_BEHAVIOR);
    }

    @Unique
    private static Optional<DispenseItemBehavior> behavior(ItemStack stack) {
        return stack.itematic$getBehavior(ItemBehaviorType.DISPENSABLE)
            .map(DispensableItemBehavior::behavior)
            .map(Holder::value);
    }
}
