package net.errorcraft.itematic.item.holder.rule;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemHolderRuleTypeKeys {
    public static final RegistryKey<ItemHolderRuleType<?>> REJECT = of("reject");
    public static final RegistryKey<ItemHolderRuleType<?>> OCCUPANCY_HELD_ITEMS_WITH_PENALTY = of("occupancy_held_items_with_penalty");
    public static final RegistryKey<ItemHolderRuleType<?>> FRACTION = of("fraction");

    private ItemHolderRuleTypeKeys() {}

    private static RegistryKey<ItemHolderRuleType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.ITEM_HOLDER_RULE_TYPE, Identifier.ofVanilla(id));
    }
}
