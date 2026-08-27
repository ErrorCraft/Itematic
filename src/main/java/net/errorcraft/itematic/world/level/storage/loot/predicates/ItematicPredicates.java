package net.errorcraft.itematic.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ItematicPredicates {
    private ItematicPredicates() {}

    public static void init() {
        register(
            "side_check",
            SideCheckPredicate.CODEC
        );
    }

    private static void register(String id, MapCodec<? extends LootItemCondition> codec) {
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            Identifier.withDefaultNamespace(id),
            codec
        );
    }
}
