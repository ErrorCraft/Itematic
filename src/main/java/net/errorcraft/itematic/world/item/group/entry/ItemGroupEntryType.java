package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.group.entry.entries.EnchantmentItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.InstrumentItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.ItemItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.PaintingVariantItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.PotionItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.SuspiciousEffectIngredientItemGroupEntry;
import net.minecraft.core.Registry;

public record ItemGroupEntryType<T extends ItemGroupEntry<T>>(MapCodec<T> codec) {
    public static final ItemGroupEntryType<ItemItemGroupEntry> ITEM = register(
        "item",
        new ItemGroupEntryType<>(ItemItemGroupEntry.CODEC)
    );
    public static final ItemGroupEntryType<PaintingVariantItemGroupEntry> PAINTING_VARIANT = register(
        "painting_variant",
        new ItemGroupEntryType<>(PaintingVariantItemGroupEntry.CODEC)
    );
    public static final ItemGroupEntryType<InstrumentItemGroupEntry> INSTRUMENT = register(
        "instrument",
        new ItemGroupEntryType<>(InstrumentItemGroupEntry.CODEC)
    );
    public static final ItemGroupEntryType<SuspiciousEffectIngredientItemGroupEntry> SUSPICIOUS_EFFECT_INGREDIENT = register(
        "suspicious_effect_ingredient",
        new ItemGroupEntryType<>(SuspiciousEffectIngredientItemGroupEntry.CODEC)
    );
    public static final ItemGroupEntryType<PotionItemGroupEntry> POTION = register(
        "potion",
        new ItemGroupEntryType<>(PotionItemGroupEntry.CODEC)
    );
    public static final ItemGroupEntryType<EnchantmentItemGroupEntry> ENCHANTMENT = register(
        "enchantment",
        new ItemGroupEntryType<>(EnchantmentItemGroupEntry.CODEC)
    );

    private static <T extends ItemGroupEntry<T>> ItemGroupEntryType<T> register(String id, ItemGroupEntryType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.ITEM_GROUP_ENTRY_TYPE, id, type);
    }
}
