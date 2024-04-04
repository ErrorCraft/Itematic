package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record MapItemColor() implements ItemColor {
    public static final MapItemColor INSTANCE = new MapItemColor();
    public static final MapCodec<MapItemColor> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return DEFAULT_COLOR;
        }
        return ColorHelper.Argb.fullAlpha(stack.getOrDefault(DataComponentTypes.MAP_COLOR, MapColorComponent.DEFAULT).rgb());
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.MAP;
    }
}
