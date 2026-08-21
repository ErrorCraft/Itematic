package net.errorcraft.itematic.mixin.advancements.criterion;

import com.google.common.collect.ImmutableList;
import net.errorcraft.itematic.access.advancements.criterion.StatePropertiesPredicateAccess;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

public class StatePropertiesPredicateExtender {
    @Mixin(StatePropertiesPredicate.Builder.class)
    public static class BuilderExtender implements StatePropertiesPredicateAccess.BuilderAccess {
        @Shadow
        @Final
        private ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers;

        @Override
        public <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate.Builder itematic$range(Property<T> property, T min, T max) {
            this.matchers.add(
                new StatePropertiesPredicate.PropertyMatcher(
                    property.getName(),
                    new StatePropertiesPredicate.RangedMatcher(
                        Optional.of(min.getSerializedName()),
                        Optional.of(max.getSerializedName())
                    )
                )
            );
            return (StatePropertiesPredicate.Builder)(Object) this;
        }
    }
}
