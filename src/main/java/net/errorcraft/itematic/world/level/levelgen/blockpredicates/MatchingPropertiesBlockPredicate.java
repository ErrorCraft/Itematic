package net.errorcraft.itematic.world.level.levelgen.blockpredicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

public class MatchingPropertiesBlockPredicate implements BlockPredicate {
    public static final MapCodec<MatchingPropertiesBlockPredicate> CODEC = StatePropertiesPredicate.CODEC.fieldOf("properties")
        .xmap(
            MatchingPropertiesBlockPredicate::new,
            predicate -> predicate.properties
        );

    private final StatePropertiesPredicate properties;

    private MatchingPropertiesBlockPredicate(StatePropertiesPredicate properties) {
        this.properties = properties;
    }

    public static MatchingPropertiesBlockPredicate of(StatePropertiesPredicate.Builder predicate) {
        return new MatchingPropertiesBlockPredicate(predicate.build().orElseThrow());
    }

    @Override
    public BlockPredicateType<?> type() {
        return ItematicBlockPredicateTypes.MATCHING_PROPERTIES;
    }

    @Override
    public boolean test(WorldGenLevel worldGenLevel, BlockPos pos) {
        return this.properties.matches(worldGenLevel.getBlockState(pos));
    }
}
