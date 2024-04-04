package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record ConstantItemColor(int color) implements ItemColor {
    public static final MapCodec<ConstantItemColor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color").forGetter(ConstantItemColor::color)
    ).apply(instance, ConstantItemColor::new));

    @Override
    public int color(ItemStack stack, int tintIndex) {
        return ColorHelper.Argb.fullAlpha(this.color);
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.CONSTANT;
    }
}
