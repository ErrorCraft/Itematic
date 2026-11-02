package net.errorcraft.itematic.world.level.levelgen.blockpredicates;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

public class ItematicBlockPredicateTypes {
    private ItematicBlockPredicateTypes() {}

    public static void bootstrap(Registry<BlockPredicateType<?>> registry) {
        Registry.register(registry, "matching_properties", MatchingPropertiesBlockPredicate.TYPE);
    }
}
