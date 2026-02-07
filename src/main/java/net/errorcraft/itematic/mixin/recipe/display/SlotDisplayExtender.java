package net.errorcraft.itematic.mixin.recipe.display;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.SequencedSet;

public interface SlotDisplayExtender {
    @Mixin(SlotDisplay.StackSlotDisplay.class)
    class StackSlotDisplayExtender {
        @Redirect(
            method = "isEnabled",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/Item;isEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"
            )
        )
        private boolean dataDrivenItemsAreAlwaysEnabled(Item instance, FeatureSet featureSet) {
            return true;
        }
    }

    @Mixin(SlotDisplay.ItemSlotDisplay.class)
    class ItemSlotDisplayExtender {
        @Redirect(
            method = "isEnabled",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/Item;isEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"
            )
        )
        private boolean dataDrivenItemsAreAlwaysEnabled(Item instance, FeatureSet featureSet) {
            return true;
        }
    }

    @Mixin(SlotDisplay.SmithingTrimSlotDisplay.class)
    class SmithingTrimSlotDisplayExtender {
        @Redirect(
            method = "appendStacks",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForIronChestplateUseRegistryEntry(ItemConvertible item, SlotDisplay.Context context) {
            return context.registries()
                .getOrThrow(RegistryKeys.ITEM)
                .getOptional(ItemKeys.IRON_CHESTPLATE)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }
    }

    @Mixin(SlotDisplay.AnyFuelSlotDisplay.class)
    class AnyFuelSlotDisplayExtender {
        @Inject(
            method = "appendStacks",
            at = @At("HEAD")
        )
        private void addDataDrivenFuel(SlotDisplay.Context context, SlotDisplay.StackConsumer consumer, CallbackInfo info) {
            context.registries()
                .getOrThrow(RegistryKeys.ITEM)
                .streamEntries()
                .filter(reference -> reference.value().itematic$hasBehavior(ItemComponentTypes.FUEL))
                .forEach(consumer::append);
        }

        @Redirect(
            method = "appendStacks",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/FuelRegistry;getFuelItems()Ljava/util/SequencedSet;"
            )
        )
        private SequencedSet<Item> doNotAddExistingFuelItems(FuelRegistry instance) {
            return Collections.emptyNavigableSet();
        }
    }
}
