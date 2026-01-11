package net.errorcraft.itematic.item.holder.rule;

import net.errorcraft.itematic.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.RejectItemHolderRule;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ItemHolderRuleTypes {
    public static final ItemHolderRuleType<RejectItemHolderRule> REJECT = register(ItemHolderRuleTypeKeys.REJECT, new ItemHolderRuleType<>(RejectItemHolderRule.CODEC, RejectItemHolderRule.PACKET_CODEC));
    public static final ItemHolderRuleType<OccupancyHeldItemsWithPenaltyItemHolderRule> OCCUPANCY_HELD_ITEMS_WITH_PENALTY = register(ItemHolderRuleTypeKeys.OCCUPANCY_HELD_ITEMS_WITH_PENALTY, new ItemHolderRuleType<>(OccupancyHeldItemsWithPenaltyItemHolderRule.CODEC, OccupancyHeldItemsWithPenaltyItemHolderRule.PACKET_CODEC));
    public static final ItemHolderRuleType<FractionItemHolderRule> FRACTION = register(ItemHolderRuleTypeKeys.FRACTION, new ItemHolderRuleType<>(FractionItemHolderRule.CODEC, FractionItemHolderRule.PACKET_CODEC));

    private ItemHolderRuleTypes() {}

    public static void init() {}

    private static <T extends ItemHolderRule> ItemHolderRuleType<T> register(RegistryKey<ItemHolderRuleType<?>> id, ItemHolderRuleType<T> type) {
        return Registry.register(ItematicRegistries.ITEM_HOLDER_RULE_TYPE, id, type);
    }
}
