package net.errorcraft.itematic.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ItematicPredicates {
    private ItematicPredicates() {}

    public static void bootstrap(Registry<MapCodec<? extends LootItemCondition>> registry) {
        Registry.register(registry, "side_check", SideCheckPredicate.CODEC);
    }
}
