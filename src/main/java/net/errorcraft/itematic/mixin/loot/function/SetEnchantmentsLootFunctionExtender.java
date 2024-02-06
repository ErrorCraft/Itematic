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
    private boolean isOfForBookUseItemComponentCheck(ItemStack instance, Item item, @Share("transformedEntry") LocalRef<RegistryEntry<Item>> transformedEntryRef) {
        Optional<RegistryEntry<Item>> transformedEntry = instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        transformedEntry.ifPresent(transformedEntryRef::set);
        return transformedEntry.isPresent();
    }

    @Redirect(
        method = "process",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForEnchantedBookUseRegistryEntry(ItemConvertible item, @Share("transformedEntry") LocalRef<RegistryEntry<Item>> transformedEntryRef) {
        return new ItemStack(transformedEntryRef.get());
    }
}
