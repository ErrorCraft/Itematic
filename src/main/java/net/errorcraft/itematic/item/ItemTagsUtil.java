package net.errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemTagsUtil {
    public static final TagKey<Item> REPAIRS_LEATHER_ARMOR = of("repairs_leather_armor");
    public static final TagKey<Item> REPAIRS_CHAINMAIL_ARMOR = of("repairs_chainmail_armor");
    public static final TagKey<Item> REPAIRS_IRON_ARMOR = of("repairs_iron_armor");
    public static final TagKey<Item> REPAIRS_GOLDEN_ARMOR = of("repairs_golden_armor");
    public static final TagKey<Item> REPAIRS_DIAMOND_ARMOR = of("repairs_diamond_armor");
    public static final TagKey<Item> REPAIRS_NETHERITE_ARMOR = of("repairs_netherite_armor");
    public static final TagKey<Item> REPAIRS_TURTLE_ARMOR = of("repairs_turtle_armor");

    public static final TagKey<Item> REPAIRS_WOODEN_TOOL = of("repairs_wooden_tool");
    public static final TagKey<Item> REPAIRS_STONE_TOOL = of("repairs_stone_tool");
    public static final TagKey<Item> REPAIRS_GOLDEN_TOOL = of("repairs_golden_tool");
    public static final TagKey<Item> REPAIRS_IRON_TOOL = of("repairs_iron_tool");
    public static final TagKey<Item> REPAIRS_DIAMOND_TOOL = of("repairs_diamond_tool");
    public static final TagKey<Item> REPAIRS_NETHERITE_TOOL = of("repairs_netherite_tool");
    public static final TagKey<Item> REPAIRS_SHIELD = of("repairs_shield");

    public static final TagKey<Item> FURNACE_MINECART_FUEL = of("furnace_minecart_fuel");
    public static final TagKey<Item> BOW_AMMUNITION = of("bow_ammunition");
    public static final TagKey<Item> CROSSBOW_AMMUNITION = of("crossbow_ammunition");

    public static final TagKey<Item> HORSE_BREEDING_ITEMS = of("horse_breeding_items");
    public static final TagKey<Item> HORSE_TEMPTING_ITEMS = of("horse_tempting_items");
    public static final TagKey<Item> CAT_BREEDING_ITEMS = of("cat_breeding_items");
    public static final TagKey<Item> CAT_TEMPTING_ITEMS = of("cat_tempting_items");
    public static final TagKey<Item> OCELOT_BREEDING_ITEMS = of("ocelot_breeding_items");
    public static final TagKey<Item> OCELOT_TEMPTING_ITEMS = of("ocelot_tempting_items");
    public static final TagKey<Item> PIG_BREEDING_ITEMS = of("pig_breeding_items");
    public static final TagKey<Item> PIG_TEMPTING_ITEMS = of("pig_tempting_items");
    public static final TagKey<Item> RABBIT_BREEDING_ITEMS = of("rabbit_breeding_items");
    public static final TagKey<Item> CHICKEN_BREEDING_ITEMS = of("chicken_breeding_items");
    public static final TagKey<Item> CHICKEN_TEMPTING_ITEMS = of("chicken_tempting_items");
    public static final TagKey<Item> PARROT_TAMING_ITEMS = of("parrot_taming_items");
    public static final TagKey<Item> COW_TEMPTING_ITEMS = of("cow_tempting_items");
    public static final TagKey<Item> SHEEP_TEMPTING_ITEMS = of("sheep_tempting_items");
    public static final TagKey<Item> LLAMA_BREEDING_ITEMS = of("llama_breeding_items");
    public static final TagKey<Item> VILLAGER_GATHERABLE_ITEMS = of("villager_gatherable_items");
    public static final TagKey<Item> FARMER_VILLAGER_GATHERABLE_ITEMS = of("farmer_villager_gatherable_items");

    private ItemTagsUtil() {}

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(id));
    }
}
