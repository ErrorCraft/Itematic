package net.errorcraft.itematic.mixin.recipe.display;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.SequencedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface SlotDisplayExtender {
    @Mixin(SlotDisplay.ItemStackSlotDisplay.class)
    class StackSlotDisplayExtender {
        @Redirect(
            method = "isEnabled",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/Item;isEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
            )
        )
        private boolean dataDrivenItemsAreAlwaysEnabled(Item instance, FeatureFlagSet featureSet) {
            return true;
        }
    }

    @Mixin(SlotDisplay.ItemSlotDisplay.class)
    class ItemSlotDisplayExtender {
        @Redirect(
            method = "isEnabled",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/Item;isEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
            )
        )
        private boolean dataDrivenItemsAreAlwaysEnabled(Item instance, FeatureFlagSet featureSet) {
            return true;
        }
    }

    @Mixin(SlotDisplay.AnyFuel.class)
    class AnyFuelSlotDisplayExtender {
        @Redirect(
            method = "resolve",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/block/entity/FuelValues;fuelItems()Ljava/util/SequencedSet;"
            )
        )
        private SequencedSet<Holder<Item>> useDataDrivenFuel(FuelValues instance, ContextMap parameters) {
            HolderLookup.Provider lookup = parameters.getOptional(SlotDisplayContext.REGISTRIES);
            if (lookup == null) {
                return Collections.emptyNavigableSet();
            }

            return lookup.lookupOrThrow(Registries.ITEM)
                .listElements()
                .filter(reference -> reference.value().itematic$hasBehavior(ItemComponentTypes.FUEL))
                .collect(Collectors.toCollection(ObjectLinkedOpenHashSet::new));
        }

        @ModifyArg(
            method = "resolve",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"
            )
        )
        private <T> Function<? super Holder<Item>, ? extends T> useRegistryEntry(Function<? super Item, ? extends T> mapper, @Local DisplayContentsFactory.ForStacks<T> fromStack) {
            return fromStack::forStack;
        }
    }
}
