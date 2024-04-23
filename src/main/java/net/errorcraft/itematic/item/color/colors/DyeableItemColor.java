package net.errorcraft.itematic.item.color.colors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.color.ItemColorType;
import net.errorcraft.itematic.item.color.ItemColorTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemStack;

public record DyeableItemColor(int index) implements ItemColor {
    public static final MapCodec<DyeableItemColor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("index").forGetter(DyeableItemColor::index)
    ).apply(instance, DyeableItemColor::new));

    public static DyeableItemColor of(int index) {
        return new DyeableItemColor(index);
    }

    @Override
    public int color(ItemStack stack, int tintIndex) {
        if (tintIndex != this.index) {
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
