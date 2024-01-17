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
    public static final ItemColorType<PotionItemColor> POTION = register(ItemColorTypeKeys.POTION, new ItemColorType<>(PotionItemColor.CODEC));
    public static final ItemColorType<FireworkItemColor> FIREWORK = register(ItemColorTypeKeys.FIREWORK, new ItemColorType<>(FireworkItemColor.CODEC));
    public static final ItemColorType<MapItemColor> MAP = register(ItemColorTypeKeys.MAP, new ItemColorType<>(MapItemColor.CODEC));

    private ItemColorTypes() {}

    public static void init() {}

    private static <T extends ItemColor> ItemColorType<T> register(RegistryKey<ItemColorType<?>> id, ItemColorType<T> itemColor) {
        return Registry.register(ItematicRegistries.ITEM_COLOR_TYPE, id, itemColor);
    }
}
