package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemStack;

public record DyeableItemColor() implements ItemColor {
    public static final DyeableItemColor INSTANCE = new DyeableItemColor();
    public static final Codec<DyeableItemColor> CODEC = Codec.unit(INSTANCE);

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex > 0) {
            return DEFAULT_COLOR;
        }
        return stack.itematic$getComponent(ItemComponentTypes.DYEABLE)
            .map(c -> c.getColor(stack))
            .orElse(DEFAULT_COLOR);
    }

    @Override
    public ItemColorType<?> getType() {
        return ItemColorTypes.DYEABLE;
    }
}
