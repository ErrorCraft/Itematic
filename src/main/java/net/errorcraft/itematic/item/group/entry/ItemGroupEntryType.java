package net.errorcraft.itematic.item.group.entry;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.group.entry.entries.*;
import net.minecraft.util.StringIdentifiable;

public enum ItemGroupEntryType implements StringIdentifiable {
    STACK("stack", StackItemGroupEntry.CODEC),
    TAG("tag", TagItemGroupEntry.CODEC),
    PAINTING_VARIANT("painting_variant", PaintingVariantItemGroupEntry.CODEC),
    INSTRUMENT("instrument", InstrumentItemGroupEntry.CODEC),
    SUSPICIOUS_EFFECT_INGREDIENT("suspicious_effect_ingredient", SuspiciousEffectIngredientItemGroupEntry.CODEC),
    POTION("potion", PotionItemGroupEntry.CODEC);

    private final String name;
    private final MapCodec<? extends ItemGroupEntry> codec;

    ItemGroupEntryType(String name, MapCodec<? extends ItemGroupEntry> codec) {
        this.name = name;
        this.codec = codec;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public MapCodec<? extends ItemGroupEntry> codec() {
        return this.codec;
    }
}
