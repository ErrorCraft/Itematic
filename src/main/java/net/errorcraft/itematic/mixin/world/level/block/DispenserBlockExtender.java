package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DispenserBlock.class)
public class DispenserBlockExtender {
    @Shadow
    @Final
    private static DefaultDispenseItemBehavior DEFAULT_BEHAVIOR;

    @WrapMethod(
        method = "getDispenseMethod"
    )
    public DispenseItemBehavior useDataComponent(Level level, ItemStack itemStack, Operation<DispenseItemBehavior> original) {
        Holder<DispenseBehavior> dispenseBehavior = itemStack.get(ItematicDataComponents.DISPENSE_BEHAVIOR);
        if (dispenseBehavior != null) {
            return dispenseBehavior.value();
        }

        return DEFAULT_BEHAVIOR;
    }
}
