package net.errorcraft.itematic.mixin.world.item.crafting.display;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
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

    @Mixin(SlotDisplay.AnyFuel.class)
    class AnyFuelExtender {
        @Redirect(
            method = "resolve",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/level/block/entity/FuelValues;fuelItems()Ljava/util/SequencedSet;"
            )
        )
        private SequencedSet<Holder<Item>> useDataDrivenFuel(FuelValues instance, ContextMap context) {
            HolderLookup.Provider lookup = context.getOptional(SlotDisplayContext.REGISTRIES);
            if (lookup == null) {
                return Collections.emptyNavigableSet();
            }

            return lookup.lookupOrThrow(Registries.ITEM)
                .listElements()
                .filter(item -> item.value().itematic$hasBehavior(ItemBehaviorType.FUEL))
                .collect(Collectors.toCollection(ObjectLinkedOpenHashSet::new));
        }

        @ModifyArg(
            method = "resolve",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"
            )
        )
        private <T> Function<? super Holder<Item>, ? extends T> useHolder(Function<? super Item, ? extends T> mapper, @Local(name = "stacks") DisplayContentsFactory.ForStacks<T> stacks) {
            return stacks::forStack;
        }
    }
}
