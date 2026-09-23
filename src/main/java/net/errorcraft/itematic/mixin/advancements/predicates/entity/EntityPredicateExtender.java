package net.errorcraft.itematic.mixin.advancements.predicates.entity;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.advancements.predicates.entity.EntityPredicateAccess;
import net.errorcraft.itematic.advancements.predicates.entity.InWaterOrRainPredicate;
import net.errorcraft.itematic.advancements.predicates.entity.UsedItemTicksPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPredicate.class)
public class EntityPredicateExtender {
    @Mixin(EntityPredicate.Builder.class)
    public static abstract class BuilderExtender implements EntityPredicateAccess.BuilderAccess {
        @Shadow
        public abstract <T extends EntitySubPredicate> EntityPredicate.Builder put(Codec<T> key, T predicate);

        @Override
        public EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            return this.put(
                UsedItemTicksPredicate.CODEC,
                new UsedItemTicksPredicate(MinMaxBounds.Ints.atLeast(ticks))
            );
        }

        @Override
        public EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            return this.put(
                InWaterOrRainPredicate.CODEC,
                new InWaterOrRainPredicate(inWaterOrRain)
            );
        }
    }
}
