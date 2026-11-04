package net.errorcraft.itematic.world.level.levelgen.blockpredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.blockpredicates.StateTestingPredicate;

public class MatchingPropertiesBlockPredicate extends StateTestingPredicate {
    public static final MapCodec<MatchingPropertiesBlockPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> stateTestingCodec(instance).and(
        StatePropertiesPredicate.CODEC.fieldOf("properties").forGetter(predicate -> predicate.properties)
    ).apply(instance, MatchingPropertiesBlockPredicate::new));
    public static final BlockPredicateType<MatchingPropertiesBlockPredicate> TYPE = () -> CODEC;

    private final StatePropertiesPredicate properties;

    private MatchingPropertiesBlockPredicate(Vec3i offset, StatePropertiesPredicate properties) {
        super(offset);
        this.properties = properties;
    }

    public static MatchingPropertiesBlockPredicate of(StatePropertiesPredicate.Builder predicate) {
        return new MatchingPropertiesBlockPredicate(Vec3i.ZERO, predicate.build().orElseThrow());
    }

    @Override
    public BlockPredicateType<?> type() {
        return TYPE;
    }

    @Override
    protected boolean test(BlockState state) {
        return this.properties.matches(state);
    }
}
