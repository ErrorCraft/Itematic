package net.errorcraft.itematic.item.color;

import net.errorcraft.itematic.item.color.colors.*;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ItemColorTypes {
    public static final ItemColorType<DyeableItemColor> DYEABLE = register(ItemColorTypeKeys.DYEABLE, new ItemColorType<>(DyeableItemColor.CODEC));
    public static final ItemColorType<IndexItemColor> INDEX = register(ItemColorTypeKeys.INDEX, new ItemColorType<>(IndexItemColor.CODEC));
    public static final ItemColorType<GrassItemColor> GRASS = register(ItemColorTypeKeys.GRASS, new ItemColorType<>(GrassItemColor.CODEC));
    public static final ItemColorType<FoliageItemColor> FOLIAGE = register(ItemColorTypeKeys.FOLIAGE, new ItemColorType<>(FoliageItemColor.CODEC));
    public static final ItemColorType<ConstantItemColor> CONSTANT = register(ItemColorTypeKeys.CONSTANT, new ItemColorType<>(ConstantItemColor.CODEC));

    private ItemColorTypes() {}

    public static void init() {}

    private static <T extends ItemColor> ItemColorType<T> register(RegistryKey<ItemColorType<?>> id, ItemColorType<T> itemColor) {
        return Registry.register(ItematicRegistries.ITEM_COLOR_TYPE, id, itemColor);
    }
}
