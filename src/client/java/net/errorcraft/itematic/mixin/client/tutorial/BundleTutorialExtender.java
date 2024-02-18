package net.errorcraft.itematic.mixin.client.tutorial;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.tutorial.BundleTutorial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTutorial.class)
public class BundleTutorialExtender {
    @Redirect(
        method = "onPickupSlotClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBundleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUNDLE);
    }
}
