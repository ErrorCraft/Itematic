package net.errorcraft.itematic.world.item.holder.rule;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.RejectItemHolderRule;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ItemHolderRuleType<T extends ItemHolderRule>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    public static final ItemHolderRuleType<RejectItemHolderRule> REJECT = register(
        "reject",
        new ItemHolderRuleType<>(RejectItemHolderRule.CODEC, RejectItemHolderRule.STREAM_CODEC)
    );
    public static final ItemHolderRuleType<OccupancyHeldItemsWithPenaltyItemHolderRule> OCCUPANCY_HELD_ITEMS_WITH_PENALTY = register(
        "occupancy_held_items_with_penalty",
        new ItemHolderRuleType<>(OccupancyHeldItemsWithPenaltyItemHolderRule.CODEC, OccupancyHeldItemsWithPenaltyItemHolderRule.STREAM_CODEC)
    );
    public static final ItemHolderRuleType<FractionItemHolderRule> FRACTION = register(
        "fraction",
        new ItemHolderRuleType<>(FractionItemHolderRule.CODEC, FractionItemHolderRule.STREAM_CODEC)
    );

    public static void init() {}

    private static <T extends ItemHolderRule> ItemHolderRuleType<T> register(String id, ItemHolderRuleType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.ITEM_HOLDER_RULE_TYPE, id, type);
    }
}
