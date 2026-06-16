package net.errorcraft.itematic.access.predicate.item;

import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.predicate.item.enchantment.EnchantmentEffectPredicate;
import net.errorcraft.itematic.predicate.item.enchantment.EnchantmentEffectPredicateType;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;

public interface ItemPredicateBuilderAccess {
    default ItemPredicate.Builder itematic$items(RegistryEntryList<Item> items) {
        return null;
    }
    default ItemPredicate.Builder itematic$behavior(ItemComponentType<?>... behavior) {
        return null;
    }
    default ItemPredicate.Builder itematic$dataComponents(ComponentType<?>... dataComponents) {
        return null;
    }
    default <T extends EnchantmentEffectPredicate> ItemPredicate.Builder itematic$enchantmentEffect(EnchantmentEffectPredicateType<T> type, T effect) {
        return null;
    }
}
