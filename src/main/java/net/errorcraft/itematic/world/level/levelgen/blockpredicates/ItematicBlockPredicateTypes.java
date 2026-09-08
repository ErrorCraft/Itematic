package net.errorcraft.itematic.world.level.levelgen.blockpredicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

public class ItematicBlockPredicateTypes {
    public static final BlockPredicateType<MatchingPropertiesBlockPredicate> MATCHING_PROPERTIES = register(
        "matching_properties",
        MatchingPropertiesBlockPredicate.CODEC
    );

    private ItematicBlockPredicateTypes() {}

    public static void init() {}

    private static <P extends BlockPredicate> BlockPredicateType<P> register(final String id, final MapCodec<P> codec) {
        return Registry.register(
            BuiltInRegistries.BLOCK_PREDICATE_TYPE,
            id,
            () -> codec
        );
    }
}
