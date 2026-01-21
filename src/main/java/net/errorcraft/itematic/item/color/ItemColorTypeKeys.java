package net.errorcraft.itematic.item.color;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemColorTypeKeys {
    public static final RegistryKey<ItemColorType<?>> DYEABLE = of("dyeable");
    public static final RegistryKey<ItemColorType<?>> INDEX = of("index");
    public static final RegistryKey<ItemColorType<?>> GRASS = of("grass");
    public static final RegistryKey<ItemColorType<?>> FOLIAGE = of("foliage");
    public static final RegistryKey<ItemColorType<?>> CONSTANT = of("constant");
    public static final RegistryKey<ItemColorType<?>> POTION = of("potion");
    public static final RegistryKey<ItemColorType<?>> FIREWORK = of("firework");
    public static final RegistryKey<ItemColorType<?>> MAP = of("map");

    private static RegistryKey<ItemColorType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ITEM_COLOR_TYPE, Identifier.ofVanilla(id));
    }
}
