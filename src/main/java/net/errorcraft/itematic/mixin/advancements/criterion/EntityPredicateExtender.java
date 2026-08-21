package net.errorcraft.itematic.mixin.advancements.criterion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.advancements.criterion.EntityPredicateAccess;
import net.errorcraft.itematic.advancements.criterion.EntityPredicates;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Function;

@Mixin(EntityPredicate.class)
public class EntityPredicateExtender implements EntityPredicateAccess {
    @Unique
    private Optional<MinMaxBounds.Ints> usedItemTicks = Optional.empty();
    @Unique
    private Optional<Boolean> inWaterOrRain = Optional.empty();

    @ModifyExpressionValue(
        method = "method_53135",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<EntityPredicate> addExtraMapCodecFields(Codec<EntityPredicate> original) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
            MinMaxBounds.Ints.CODEC.optionalFieldOf("used_item_ticks").forGetter(EntityPredicate::itematic$usedItemTicks),
            Codec.BOOL.optionalFieldOf("in_water_or_rain").forGetter(EntityPredicate::itematic$inWaterOrRain)
        ).apply(instance, EntityPredicates::setFields));
    }

    @ModifyReturnValue(
        method = "matches(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;)Z",
        at = @At("TAIL")
    )
    private boolean testExtraFields(boolean original, ServerLevel level, Vec3 pos, Entity entity) {
        if (!original) {
            return false;
        }

        if (this.usedItemTicks.isPresent() && entity instanceof LivingEntity livingEntity && !this.usedItemTicks.get().matches(livingEntity.itematic$usedItemTicks())) {
            return false;
        }

        return this.inWaterOrRain.isEmpty() || entity.isInWaterOrRain() == this.inWaterOrRain.get();
    }

    @Override
    public Optional<MinMaxBounds.Ints> itematic$usedItemTicks() {
        return this.usedItemTicks;
    }

    @Override
    public void itematic$setUsedItemTicks(Optional<MinMaxBounds.Ints> usedItemTicks) {
        this.usedItemTicks = usedItemTicks;
    }

    @Override
    public Optional<Boolean> itematic$inWaterOrRain() {
        return this.inWaterOrRain;
    }

    @Override
    public void itematic$setInWaterOrRain(Optional<Boolean> inWaterOrRain) {
        this.inWaterOrRain = inWaterOrRain;
    }

    @Mixin(EntityPredicate.Builder.class)
    public static class BuilderExtender implements BuilderAccess {
        @Unique
        private MinMaxBounds.@Nullable Ints usedItemTicks;
        @Unique
        @Nullable
        private Boolean inWaterOrRain;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private EntityPredicate setExtraFields(EntityPredicate original) {
            return EntityPredicates.setFields(
                original,
                Optional.ofNullable(this.usedItemTicks),
                Optional.ofNullable(this.inWaterOrRain)
            );
        }

        @Override
        public EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            this.usedItemTicks = MinMaxBounds.Ints.atLeast(ticks);
            return (EntityPredicate.Builder)(Object) this;
        }

        @Override
        public EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            this.inWaterOrRain = inWaterOrRain;
            return (EntityPredicate.Builder)(Object) this;
        }
    }
}
