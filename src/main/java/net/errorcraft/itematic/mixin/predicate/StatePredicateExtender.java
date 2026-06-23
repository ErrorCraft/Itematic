package net.errorcraft.itematic.mixin.predicate;

import com.google.common.collect.ImmutableList;
import net.errorcraft.itematic.access.predicate.StatePredicateAccess;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

public class StatePredicateExtender {
    @Mixin(StatePredicate.Builder.class)
    public static class BuilderExtender implements StatePredicateAccess.BuilderAccess {
        @Shadow
        @Final
        private ImmutableList.Builder<StatePredicate.Condition> conditions;

        @Override
        public <T extends Comparable<T> & StringIdentifiable> StatePredicate.Builder itematic$range(Property<T> property, T min, T max) {
            this.conditions.add(
                new StatePredicate.Condition(
                    property.getName(),
                    new StatePredicate.RangedValueMatcher(Optional.of(min.asString()), Optional.of(max.asString()))
                )
            );
            return (StatePredicate.Builder)(Object) this;
        }
    }
}
