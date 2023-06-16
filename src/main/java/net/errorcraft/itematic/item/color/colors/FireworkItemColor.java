package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public record FireworkItemColor() implements ItemColor {
    public static final Codec<FireworkItemColor> CODEC = Codec.unit(new FireworkItemColor());
    private static final int DEFAULT_FIREWORK_COLOR = 0x8a8a8a;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) {
            return DEFAULT_COLOR;
        }

        int[] colors = getExplosionColors(stack);
        if (colors == null || colors.length == 0) {
            return DEFAULT_FIREWORK_COLOR;
        }

        if (colors.length == 1) {
            return colors[0];
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        for (int color : colors) {
            red += (color & 0xff0000) >> 16;
            green += (color & 0x00ff00) >> 8;
            blue += (color & 0x0000ff);
        }

        red /= colors.length;
        green /= colors.length;
        blue /= colors.length;
        return red << 16 | green << 8 | blue;
    }

    @Override
    public ItemColorType<?> getType() {
        return ItemColorTypes.FIREWORK;
    }

    private static int[] getExplosionColors(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(FireworkRocketItem.EXPLOSION_KEY);
        if (nbt == null || !nbt.contains(FireworkRocketItem.COLORS_KEY, NbtElement.INT_ARRAY_TYPE)) {
            return null;
        }
        return nbt.getIntArray(FireworkRocketItem.COLORS_KEY);
    }
}
