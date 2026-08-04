package net.errorcraft.itematic.loot.predicate;

import net.errorcraft.itematic.mixin.loot.condition.LootConditionTypesAccessor;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ItematicPredicateTypes {
    public static final LootItemConditionType SIDE_CHECK = LootConditionTypesAccessor.register("side_check", SideCheckPredicate.CODEC);

    private ItematicPredicateTypes() {}

    public static void init() {}
}
