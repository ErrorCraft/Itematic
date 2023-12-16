package net.errorcraft.itematic.loot.predicate;

import net.errorcraft.itematic.mixin.loot.condition.LootConditionTypesAccessor;
import net.minecraft.loot.condition.LootConditionType;

public class ItematicPredicateTypes {
    public static final LootConditionType SIDE_CHECK = LootConditionTypesAccessor.register("side_check", SideCheckPredicate.CODEC);

    private ItematicPredicateTypes() {}

    public static void init() {}
}
