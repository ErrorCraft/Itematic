package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record PotionItemColor() implements ItemColor {
    public static final MapCodec<PotionItemColor> CODEC = MapCodec.unit(new PotionItemColor());

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) {
            return DEFAULT_COLOR;
        }
        return ColorHelper.Argb.fullAlpha(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getColor());
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.POTION;
    }
}
