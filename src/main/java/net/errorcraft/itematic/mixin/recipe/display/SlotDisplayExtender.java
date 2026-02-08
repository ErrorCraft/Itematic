package net.errorcraft.itematic.mixin.recipe.display;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.SequencedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Mixin(SlotDisplay.AnyFuelSlotDisplay.class)
    class AnyFuelSlotDisplayExtender {
        @Redirect(
            method = "appendStacks",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/FuelRegistry;getFuelItems()Ljava/util/SequencedSet;"
            )
        )
        private SequencedSet<RegistryEntry<Item>> useDataDrivenFuel(FuelRegistry instance, ContextParameterMap parameters) {
            RegistryWrapper.WrapperLookup lookup = parameters.getNullable(SlotDisplayContexts.REGISTRIES);
            if (lookup == null) {
                return Collections.emptyNavigableSet();
            }

            return lookup.getOrThrow(RegistryKeys.ITEM)
                .streamEntries()
                .filter(reference -> reference.value().itematic$hasBehavior(ItemComponentTypes.FUEL))
                .collect(Collectors.toCollection(ObjectLinkedOpenHashSet::new));
        }

        @ModifyArg(
            method = "appendStacks",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"
            )
        )
        private <T> Function<? super RegistryEntry<Item>, ? extends T> useRegistryEntry(Function<? super Item, ? extends T> mapper, @Local DisplayedItemFactory.FromStack<T> fromStack) {
            return fromStack::toDisplayed;
        }
    }
}
