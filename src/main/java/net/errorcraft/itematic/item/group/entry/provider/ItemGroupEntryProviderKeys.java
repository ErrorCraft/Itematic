package net.errorcraft.itematic.item.group.entry.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemGroupEntryProviderKeys {
    public static final RegistryKey<ItemGroupEntryProvider> BUILDING_BLOCKS = of("building_blocks");
    public static final RegistryKey<ItemGroupEntryProvider> COLORED_BLOCKS = of("colored_blocks");
    public static final RegistryKey<ItemGroupEntryProvider> NATURAL_BLOCKS = of("natural_blocks");
    public static final RegistryKey<ItemGroupEntryProvider> FUNCTIONAL_BLOCKS = of("functional_blocks");
    public static final RegistryKey<ItemGroupEntryProvider> REDSTONE_BLOCKS = of("redstone_blocks");
    public static final RegistryKey<ItemGroupEntryProvider> TOOLS_AND_UTILITIES = of("tools_and_utilities");
    public static final RegistryKey<ItemGroupEntryProvider> COMBAT = of("combat");
    public static final RegistryKey<ItemGroupEntryProvider> FOOD_AND_DRINKS = of("food_and_drinks");
    public static final RegistryKey<ItemGroupEntryProvider> INGREDIENTS = of("ingredients");
    public static final RegistryKey<ItemGroupEntryProvider> SPAWN_EGGS = of("spawn_eggs");
    public static final RegistryKey<ItemGroupEntryProvider> OP_BLOCKS = of("op_blocks");

    private ItemGroupEntryProviderKeys() {}

    private static RegistryKey<ItemGroupEntryProvider> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, Identifier.ofVanilla(id));
    }
}
