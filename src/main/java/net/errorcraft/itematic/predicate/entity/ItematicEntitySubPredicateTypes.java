package net.errorcraft.itematic.predicate.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ItematicEntitySubPredicateTypes {
    public static final MapCodec<VillagerEntitySubPredicate> VILLAGER = register("villager", VillagerEntitySubPredicate.CODEC);

    private ItematicEntitySubPredicateTypes() {}

    public static void init() {}

    private static <T extends EntitySubPredicate> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, id, codec);
    }
}
