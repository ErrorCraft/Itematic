package net.errorcraft.itematic.advancements.criterion;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ItematicEntitySubPredicates {
    public static final MapCodec<VillagerPredicate> VILLAGER = register(
        "villager",
        VillagerPredicate.CODEC
    );

    private ItematicEntitySubPredicates() {}

    public static void init() {}

    private static <T extends EntitySubPredicate> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, id, codec);
    }
}
