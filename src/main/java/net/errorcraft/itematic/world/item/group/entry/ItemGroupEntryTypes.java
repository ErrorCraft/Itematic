package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.group.entry.entries.EnchantmentEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.InstrumentEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.ItemEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.PaintingVariantEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.PotionEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.SuspiciousEffectIngredientEntry;
import net.minecraft.core.Registry;

public class ItemGroupEntryTypes {
    private ItemGroupEntryTypes() {}

    public static void bootstrap(Registry<MapCodec<? extends ItemGroupEntry<?>>> registry) {
        Registry.register(registry, "item", ItemEntry.CODEC);
        Registry.register(registry, "enchantment", EnchantmentEntry.CODEC);
        Registry.register(registry, "potion", PotionEntry.CODEC);
        Registry.register(registry, "instrument", InstrumentEntry.CODEC);
        Registry.register(registry, "painting_variant", PaintingVariantEntry.CODEC);
        Registry.register(registry, "suspicious_effect_ingredient", SuspiciousEffectIngredientEntry.CODEC);
    }
}
