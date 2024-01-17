package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;

public record MapItemColor() implements ItemColor {
    public static final MapItemColor INSTANCE = new MapItemColor();
    public static final Codec<MapItemColor> CODEC = Codec.unit(INSTANCE);

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return DEFAULT_COLOR;
        }
        return FilledMapItem.getMapColor(stack);
    }

    @Override
    public ItemColorType<?> getType() {
        return ItemColorTypes.MAP;
    }
}
