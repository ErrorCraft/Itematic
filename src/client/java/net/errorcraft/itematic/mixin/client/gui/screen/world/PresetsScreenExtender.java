package net.errorcraft.itematic.mixin.client.gui.screen.world;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class PresetsScreenExtender {
    public static class SuperflatPresetsListWidgetExtender {
        @Mixin(targets = "net.minecraft.client.gui.screen.world.PresetsScreen$SuperflatPresetsListWidget$SuperflatPresetEntry")
        public static class SuperflatPresetEntryExtender {
            @Shadow
            @Final
            private FlatLevelGeneratorPreset preset;

            @Redirect(
                method = "renderIcon",
                at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
                )
            )
            private ItemStack newItemStackUseRegistryEntry(ItemConvertible item) {
                return new ItemStack(this.preset.displayItem());
            }
        }
    }
}
