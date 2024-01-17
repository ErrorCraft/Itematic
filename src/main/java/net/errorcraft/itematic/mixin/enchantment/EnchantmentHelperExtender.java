package net.errorcraft.itematic.mixin.enchantment;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Optional;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperExtender {
    @Redirect(
        method = { "get", "set" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.ENCHANTMENT_HOLDER);
    }

    @Redirect(
        method = "getPossibleEntries",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;iterator()Ljava/util/Iterator;"
        )
    )
    private static Iterator<Enchantment> getPossibleEntriesUseEnchantmentTag(Registry<Enchantment> instance, int power, ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::enchantments)
            .map(key -> instance.getOrCreateEntryList(key).stream().map(RegistryEntry::value).iterator())
            .orElse(instance.iterator());
    }

    @Redirect(
        method = "getPossibleEntries",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean getPossibleEntriesItemIsAlwaysAcceptable(EnchantmentTarget instance, Item item) {
        return true;
    }

    @Redirect(
        method = "enchant",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForBookUseItemComponent(ItemStack instance, Item item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        Optional<RegistryEntry<Item>> optionalEntry = instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        optionalEntry.ifPresent(transformsInto::set);
        return optionalEntry.isPresent();
    }

    @Redirect(
        method = "enchant",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseItemComponent(ItemConvertible item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        return new ItemStack(transformsInto.get());
    }
}
