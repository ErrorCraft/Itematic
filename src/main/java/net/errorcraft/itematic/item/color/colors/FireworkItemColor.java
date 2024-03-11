package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntList;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record FireworkItemColor() implements ItemColor {
    public static final Codec<FireworkItemColor> CODEC = Codec.unit(new FireworkItemColor());
    private static final int DEFAULT_FIREWORK_COLOR = 0xff8a8a8a;

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) {
            return DEFAULT_COLOR;
        }

        IntList colors = stack.getOrDefault(DataComponentTypes.FIREWORK_EXPLOSION, FireworkExplosionComponent.DEFAULT).colors();
        if (colors.isEmpty()) {
            return DEFAULT_FIREWORK_COLOR;
        }

        int size = colors.size();
        if (size == 1) {
            return ColorHelper.Argb.fullAlpha(colors.getInt(0));
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        for (int color : colors) {
            red += ColorHelper.Argb.getRed(color);
            green += ColorHelper.Argb.getGreen(color);
            blue += ColorHelper.Argb.getBlue(color);
        }

        return ColorHelper.Argb.getArgb(red / size, green / size, blue / size);
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.FIREWORK;
    }
}
