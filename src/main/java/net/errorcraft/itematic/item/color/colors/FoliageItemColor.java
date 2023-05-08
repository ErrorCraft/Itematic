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
import net.minecraft.world.biome.Biome;

public record FoliageItemColor(RegistryEntry<Biome> biome) implements ItemColor {
    public static final Codec<FoliageItemColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BIOME).fieldOf("biome").forGetter(FoliageItemColor::biome)
    ).apply(instance, FoliageItemColor::new));

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return this.biome.value().getFoliageColor();
    }

    @Override
    public ItemColorType<?> getType() {
        return ItemColorTypes.FOLIAGE;
    }
}
