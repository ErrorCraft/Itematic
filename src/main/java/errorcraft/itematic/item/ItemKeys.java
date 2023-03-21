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
    public static final RegistryKey<Item> IRON_SWORD = register("iron_sword");
    public static final RegistryKey<Item> IRON_PICKAXE = register("iron_pickaxe");
    public static final RegistryKey<Item> MUSHROOM_STEW = register("mushroom_stew");
    public static final RegistryKey<Item> FEATHER = register("feather");
    public static final RegistryKey<Item> BREAD = register("bread");
    public static final RegistryKey<Item> IRON_HELMET = register("iron_helmet");
    public static final RegistryKey<Item> IRON_CHESTPLATE = register("iron_chestplate");
    public static final RegistryKey<Item> IRON_LEGGINGS = register("iron_leggings");
    public static final RegistryKey<Item> IRON_BOOTS = register("iron_boots");
    public static final RegistryKey<Item> PORKCHOP = register("porkchop");
    public static final RegistryKey<Item> COOKED_PORKCHOP = register("cooked_porkchop");
    public static final RegistryKey<Item> GOLDEN_APPLE = register("golden_apple");
    public static final RegistryKey<Item> ENCHANTED_GOLDEN_APPLE = register("enchanted_golden_apple");
    public static final RegistryKey<Item> WATER_BUCKET = register("water_bucket");
    public static final RegistryKey<Item> COD = register("cod");
    public static final RegistryKey<Item> SALMON = register("salmon");
    public static final RegistryKey<Item> TROPICAL_FISH = register("tropical_fish");
    public static final RegistryKey<Item> PUFFERFISH = register("pufferfish");
    public static final RegistryKey<Item> COOKED_COD = register("cooked_cod");
    public static final RegistryKey<Item> COOKED_SALMON = register("cooked_salmon");
    public static final RegistryKey<Item> COOKIE = register("cookie");
    public static final RegistryKey<Item> MELON_SLICE = register("melon_slice");
    public static final RegistryKey<Item> DRIED_KELP = register("dried_kelp");
    public static final RegistryKey<Item> BEEF = register("beef");
    public static final RegistryKey<Item> COOKED_BEEF = register("cooked_beef");
    public static final RegistryKey<Item> CHICKEN = register("chicken");
    public static final RegistryKey<Item> COOKED_CHICKEN = register("cooked_chicken");
    public static final RegistryKey<Item> ROTTEN_FLESH = register("rotten_flesh");
    public static final RegistryKey<Item> SPIDER_EYE = register("spider_eye");
    public static final RegistryKey<Item> PIG_SPAWN_EGG = register("pig_spawn_egg");
    public static final RegistryKey<Item> CARROT = register("carrot");
    public static final RegistryKey<Item> POTATO = register("potato");
    public static final RegistryKey<Item> BAKED_POTATO = register("baked_potato");
    public static final RegistryKey<Item> POISONOUS_POTATO = register("poisonous_potato");
    public static final RegistryKey<Item> GOLDEN_CARROT = register("golden_carrot");
    public static final RegistryKey<Item> PUMPKIN_PIE = register("pumpkin_pie");
    public static final RegistryKey<Item> RABBIT = register("rabbit");
    public static final RegistryKey<Item> COOKED_RABBIT = register("cooked_rabbit");
    public static final RegistryKey<Item> RABBIT_STEW = register("rabbit_stew");
    public static final RegistryKey<Item> MUTTON = register("mutton");
    public static final RegistryKey<Item> COOKED_MUTTON = register("cooked_mutton");
    public static final RegistryKey<Item> CHORUS_FRUIT = register("chorus_fruit");
    public static final RegistryKey<Item> BEETROOT = register("beetroot");
    public static final RegistryKey<Item> BEETROOT_SOUP = register("beetroot_soup");
    public static final RegistryKey<Item> SHIELD = register("shield");
    public static final RegistryKey<Item> SUSPICIOUS_STEW = register("suspicious_stew");
    public static final RegistryKey<Item> SWEET_BERRIES = register("sweet_berries");
    public static final RegistryKey<Item> GLOW_BERRIES = register("glow_berries");
    public static final RegistryKey<Item> HONEY_BOTTLE = register("honey_bottle");


    private static RegistryKey<Item> register(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, new Identifier(name));
    }
}
