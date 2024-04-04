package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemStack;

public record DyeableItemColor() implements ItemColor {
    public static final DyeableItemColor INSTANCE = new DyeableItemColor();
    public static final MapCodec<DyeableItemColor> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex > 0) {
            return DEFAULT_COLOR;
        }
        return stack.itematic$getComponent(ItemComponentTypes.DYEABLE)
            .map(c -> c.getColor(stack))
            .orElse(DEFAULT_COLOR);
    }

    @Override
    public ItemColorType<?> type() {
        return ItemColorTypes.DYEABLE;
    }
}
