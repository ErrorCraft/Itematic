package net.errorcraft.itematic.item.color.colors;

import com.google.common.primitives.Ints;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

import java.util.List;

public record IndexItemColor(List<Integer> indices) implements ItemColor {
    public static final MapCodec<IndexItemColor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.listOf().fieldOf("indices").forGetter(IndexItemColor::indices)
    ).apply(instance, IndexItemColor::new));

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex < 0 || tintIndex >= indices.size()) {
            return DEFAULT_COLOR;
        }
        return ColorHelper.Argb.fullAlpha(this.indices.get(tintIndex));
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.INDEX;
    }

    public static IndexItemColor of(int... indices) {
        return new IndexItemColor(Ints.asList(indices));
    }
}
