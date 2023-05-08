package net.errorcraft.itematic.item.color;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.item.ItemStack;

public interface ItemColor {
    Codec<ItemColor> CODEC = ItematicRegistries.ITEM_COLOR_TYPE.getCodec().dispatch(ItemColor::getType, ItemColorType::codec);
    int DEFAULT_COLOR = -1;

    int getColor(ItemStack stack, int tintIndex);
    ItemColorType<?> getType();
}
