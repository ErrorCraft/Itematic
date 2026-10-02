package net.errorcraft.itematic.mixin.world.item.crafting.display;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public interface SlotDisplayExtender {
    @Mixin({
        SlotDisplay.ItemStackSlotDisplay.class,
        SlotDisplay.ItemSlotDisplay.class
    })
    class ItemSlotDisplaysExtender {
        @Redirect(
            method = "isEnabled",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/Item;isEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
            )
        )
        private boolean dataDrivenItemsAreAlwaysEnabled(Item instance, FeatureFlagSet enabledFeatures) {
            return true;
        }
    }
}
