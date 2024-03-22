package net.errorcraft.itematic.mixin.block.dispenser;

import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShearsDispenserBehavior.class)
public class ShearsDispenserBehaviorExtender {
    @Redirect(
        method = "method_56167",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setCount(I)V"
        )
    )
    private static void decrementItemStackInsteadOfSettingCount(ItemStack instance, int count) {
        instance.decrement(1);
    }
}
