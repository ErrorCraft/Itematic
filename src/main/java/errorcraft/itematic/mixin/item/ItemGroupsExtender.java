package errorcraft.itematic.mixin.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemGroupsExtender {
    @Mixin(targets = "net/minecraft/item/ItemGroups$1")
    public static abstract class BuildingBlocksExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$5")
    public static class NaturalExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$6")
    static class FunctionalExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$7")
    static class RedstoneExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$10")
    static class ToolsExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$11")
    static class CombatExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$12")
    static class ConsumablesExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$2")
    static class CraftingExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroups$3")
    static class SpawnEggsExtender {
        @Inject(
            method = "addItems",
            at = @At("HEAD"),
            cancellable = true
        )
        private void removeItems(FeatureSet enabledFeatures, ItemGroup.Entries entries, CallbackInfo info) {
            info.cancel();
        }
    }
}
