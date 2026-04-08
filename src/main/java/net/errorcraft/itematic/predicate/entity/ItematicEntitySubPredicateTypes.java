package net.errorcraft.itematic.predicate.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.predicate.entity.EntitySubPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItematicEntitySubPredicateTypes {
    public static final MapCodec<VillagerEntitySubPredicate> VILLAGER = register("villager", VillagerEntitySubPredicate.CODEC);

    private ItematicEntitySubPredicateTypes() {}

    public static void init() {}

    private static <T extends EntitySubPredicate> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENTITY_SUB_PREDICATE_TYPE, id, codec);
    }
}
