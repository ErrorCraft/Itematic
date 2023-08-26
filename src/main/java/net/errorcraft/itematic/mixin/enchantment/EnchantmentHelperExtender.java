package net.errorcraft.itematic.mixin.enchantment;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

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
        return instance.hasComponent(ItemComponentTypes.ENCHANTMENT_HOLDER);
    }

    @Redirect(
        method = "getPossibleEntries",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;iterator()Ljava/util/Iterator;"
        )
    )
    private static Iterator<Enchantment> getPossibleEntriesUseEnchantmentTag(Registry<Enchantment> instance, int power, ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.ENCHANTABLE)
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
}
