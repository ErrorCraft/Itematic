package net.errorcraft.itematic.mixin.client.gui.screen.world;

import net.minecraft.client.gui.screens.PresetFlatWorldScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class PresetsScreenExtender {
    public static class SuperflatPresetsListWidgetExtender {
        @Mixin(PresetFlatWorldScreen.PresetsList.Entry.class)
        public static class SuperflatPresetEntryExtender {
            @Shadow
            @Final
            private FlatLevelGeneratorPreset preset;

            @Redirect(
                method = "blitSlot",
                at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
                )
            )
            private ItemStack newItemStackUseRegistryEntry(ItemLike item) {
                return new ItemStack(this.preset.displayItem());
            }
        }
    }
}
