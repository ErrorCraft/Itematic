package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

public record PotionItemColor() implements ItemColor {
    public static final Codec<PotionItemColor> CODEC = Codec.unit(new PotionItemColor());

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) {
            return DEFAULT_COLOR;
        }
        return PotionUtil.getColor(stack);
    }

    @Override
    public ItemColorType<?> getType() {
        return ItemColorTypes.POTION;
    }
}
