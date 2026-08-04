package net.errorcraft.itematic.item.holder.rule;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ItemHolderRuleTypeKeys {
    public static final ResourceKey<ItemHolderRuleType<?>> REJECT = of("reject");
    public static final ResourceKey<ItemHolderRuleType<?>> OCCUPANCY_HELD_ITEMS_WITH_PENALTY = of("occupancy_held_items_with_penalty");
    public static final ResourceKey<ItemHolderRuleType<?>> FRACTION = of("fraction");

    private ItemHolderRuleTypeKeys() {}

    private static ResourceKey<ItemHolderRuleType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.ITEM_HOLDER_RULE_TYPE, Identifier.withDefaultNamespace(id));
    }
}
