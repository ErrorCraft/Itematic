package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;

public record PotionItemColor() implements ItemColor<PotionItemColor> {
    public static final PotionItemColor INSTANCE = new PotionItemColor();
    public static final MapCodec<PotionItemColor> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ItemColorType<PotionItemColor> type() {
        return ItemColorTypes.POTION;
    }

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex != 0) {
            return DEFAULT_COLOR;
        }

        return ColorHelper.fullAlpha(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getColor());
    }
}
