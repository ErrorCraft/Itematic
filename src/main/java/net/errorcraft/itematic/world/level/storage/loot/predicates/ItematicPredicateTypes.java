package net.errorcraft.itematic.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ItematicPredicateTypes {
    public static final LootItemConditionType SIDE_CHECK = register(
        "side_check",
        SideCheckPredicate.CODEC
    );

    private ItematicPredicateTypes() {}

    public static void init() {}

    private static LootItemConditionType register(String id, MapCodec<? extends LootItemCondition> codec) {
        return Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            Identifier.withDefaultNamespace(id),
            new LootItemConditionType(codec)
        );
    }
}
