package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ItemKeys {
    public static final RegistryKey<Item> AIR = register("air");
    public static final RegistryKey<Item> STONE = register("stone");
    public static final RegistryKey<Item> GRASS_BLOCK = register("grass_block");
    public static final RegistryKey<Item> SAND = register("sand");
    public static final RegistryKey<Item> GRASS = register("grass");
    public static final RegistryKey<Item> OAK_SLAB = register("oak_slab");
    public static final RegistryKey<Item> SNOW = register("snow");
    public static final RegistryKey<Item> BARRIER = register("barrier");
    public static final RegistryKey<Item> REDSTONE = register("redstone");
    public static final RegistryKey<Item> APPLE = register("apple");
    public static final RegistryKey<Item> IRON_PICKAXE = register("iron_pickaxe");
    public static final RegistryKey<Item> FEATHER = register("feather");
    public static final RegistryKey<Item> WATER_BUCKET = register("water_bucket");
    public static final RegistryKey<Item> PIG_SPAWN_EGG = register("pig_spawn_egg");

    private static RegistryKey<Item> register(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, new Identifier(name));
    }
}
