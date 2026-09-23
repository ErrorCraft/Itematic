package net.errorcraft.itematic.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.Registry;

public class ItematicEntitySubPredicates {
    private ItematicEntitySubPredicates() {}

    public static void bootstrap(Registry<Codec<? extends EntitySubPredicate>> registry) {
        Registry.register(registry, "used_item_ticks", UsedItemTicksPredicate.CODEC);
        Registry.register(registry, "in_water_or_rain", InWaterOrRainPredicate.CODEC);
    }
}
