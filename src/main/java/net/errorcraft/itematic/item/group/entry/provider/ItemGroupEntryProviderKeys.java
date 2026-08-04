package net.errorcraft.itematic.item.group.entry.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ItemGroupEntryProviderKeys {
    public static final ResourceKey<ItemGroupEntryProvider> BUILDING_BLOCKS = of("building_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> COLORED_BLOCKS = of("colored_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> NATURAL_BLOCKS = of("natural_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> FUNCTIONAL_BLOCKS = of("functional_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> REDSTONE_BLOCKS = of("redstone_blocks");
    public static final ResourceKey<ItemGroupEntryProvider> TOOLS_AND_UTILITIES = of("tools_and_utilities");
    public static final ResourceKey<ItemGroupEntryProvider> COMBAT = of("combat");
    public static final ResourceKey<ItemGroupEntryProvider> FOOD_AND_DRINKS = of("food_and_drinks");
    public static final ResourceKey<ItemGroupEntryProvider> INGREDIENTS = of("ingredients");
    public static final ResourceKey<ItemGroupEntryProvider> SPAWN_EGGS = of("spawn_eggs");
    public static final ResourceKey<ItemGroupEntryProvider> OP_BLOCKS = of("op_blocks");

    private ItemGroupEntryProviderKeys() {}

    private static ResourceKey<ItemGroupEntryProvider> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, Identifier.withDefaultNamespace(id));
    }
}
