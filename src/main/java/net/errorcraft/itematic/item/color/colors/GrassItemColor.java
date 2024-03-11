package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.biome.Biome;

public record GrassItemColor(RegistryEntry<Biome> biome) implements ItemColor {
    public static final Codec<GrassItemColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BIOME).fieldOf("biome").forGetter(GrassItemColor::biome)
    ).apply(instance, GrassItemColor::new));

    @Override
    public int color(ItemStack stack, int tintIndex) {
        return ColorHelper.Argb.fullAlpha(this.biome.value().getGrassColorAt(0.0d, 0.0d));
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.GRASS;
    }
}
