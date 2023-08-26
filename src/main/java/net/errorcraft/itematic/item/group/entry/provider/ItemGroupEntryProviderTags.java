package net.errorcraft.itematic.item.group.entry.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemGroupEntryProviderTags {
    public static final TagKey<ItemGroupEntryProvider> BUILDING_BLOCKS = of("building_blocks");
    public static final TagKey<ItemGroupEntryProvider> COLORED_BLOCKS = of("colored_blocks");
    public static final TagKey<ItemGroupEntryProvider> NATURAL_BLOCKS = of("natural_blocks");
    public static final TagKey<ItemGroupEntryProvider> FUNCTIONAL_BLOCKS = of("functional_blocks");
    public static final TagKey<ItemGroupEntryProvider> REDSTONE_BLOCKS = of("redstone_blocks");
    public static final TagKey<ItemGroupEntryProvider> TOOLS_AND_UTILITIES = of("tools_and_utilities");
    public static final TagKey<ItemGroupEntryProvider> COMBAT = of("combat");
    public static final TagKey<ItemGroupEntryProvider> FOOD_AND_DRINKS = of("food_and_drinks");
    public static final TagKey<ItemGroupEntryProvider> INGREDIENTS = of("ingredients");
    public static final TagKey<ItemGroupEntryProvider> SPAWN_EGGS = of("spawn_eggs");
    public static final TagKey<ItemGroupEntryProvider> OP_BLOCKS = of("op_blocks");

    private ItemGroupEntryProviderTags() {}

    private static TagKey<ItemGroupEntryProvider> of(String id) {
        return TagKey.of(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, new Identifier(id));
    }
}
