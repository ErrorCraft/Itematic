package net.errorcraft.itematic.item.color;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.item.ItemStack;

public interface ItemColor {
    Codec<ItemColor> CODEC = ItematicRegistries.ITEM_COLOR_TYPE.getCodec().dispatch(ItemColor::type, ItemColorType::codec);
    int DEFAULT_COLOR = 0xffffffff;

    int color(ItemStack stack, int tintIndex);
    ItemColorType<?> type();
}
