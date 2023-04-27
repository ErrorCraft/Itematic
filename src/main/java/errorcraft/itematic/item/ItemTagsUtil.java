package errorcraft.itematic.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemTagsUtil {
    public static final TagKey<Item> REPAIRS_IRON_ARMOR = of("repairs_iron_armor");

    public static final TagKey<Item> REPAIRS_WOODEN_TOOL = of("repairs_wooden_tool");
    public static final TagKey<Item> REPAIRS_STONE_TOOL = of("repairs_stone_tool");
    public static final TagKey<Item> REPAIRS_GOLDEN_TOOL = of("repairs_golden_tool");
    public static final TagKey<Item> REPAIRS_IRON_TOOL = of("repairs_iron_tool");
    public static final TagKey<Item> REPAIRS_DIAMOND_TOOL = of("repairs_diamond_tool");
    public static final TagKey<Item> REPAIRS_NETHERITE_TOOL = of("repairs_netherite_tool");
    public static final TagKey<Item> REPAIRS_SHIELD = of("repairs_shield");

    public static final TagKey<Item> FURNACE_MINECART_FUEL = of("furnace_minecart_fuel");

    private ItemTagsUtil() {}

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(id));
    }
}
