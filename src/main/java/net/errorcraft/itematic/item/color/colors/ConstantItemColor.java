package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record ConstantItemColor(int color) implements ItemColor<ConstantItemColor> {
    public static final MapCodec<ConstantItemColor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color").forGetter(ConstantItemColor::color)
    ).apply(instance, ConstantItemColor::new));

    public static ConstantItemColor of(int color) {
        return new ConstantItemColor(color);
    }

    @Override
    public ItemColorType<ConstantItemColor> type() {
        return ItemColorTypes.CONSTANT;
    }

    @Override
    public int color(ItemStack stack, int tintIndex) {
        return ColorHelper.Argb.fullAlpha(this.color);
    }
}
