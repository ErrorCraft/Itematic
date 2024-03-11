package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionExtender {
    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemComponentCheck(ItemStack instance, Item item, @Share("transformedEntry") LocalRef<RegistryEntry<Item>> transformedEntry) {
        Optional<RegistryEntry<Item>> optionalEntry = instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        optionalEntry.ifPresent(transformedEntry::set);
        return optionalEntry.isPresent();
    }

    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyComponentsToNewStack(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack copyComponentsToNewStackForEnchantedBookUseRegistryEntry(ItemStack instance, ItemConvertible item, int count, @Share("transformedEntry") LocalRef<RegistryEntry<Item>> transformedEntry) {
        return instance.itematic$copyComponentsToNewStack(transformedEntry.get(), count);
    }
}
