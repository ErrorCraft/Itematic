package net.errorcraft.itematic.mixin.predicate.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.predicate.EntityPredicateAccess;
import net.errorcraft.itematic.access.predicate.EntityPredicateBuilderAccess;
import net.errorcraft.itematic.predicate.EntityPredicateExtraFields;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(EntityPredicate.class)
public class EntityPredicateExtender implements EntityPredicateAccess {
    @Unique
    private EntityPredicateExtraFields extraFields;

    @Redirect(
        method = "method_53135",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<EntityPredicate> createCodecAddExtraFields(Function<RecordCodecBuilder.Instance<EntityPredicate>, ? extends App<RecordCodecBuilder.Mu<EntityPredicate>, EntityPredicate>> builder) {
        MapCodec<EntityPredicate> mapCodec = RecordCodecBuilder.mapCodec(builder);
        return mapCodec.dependent(EntityPredicateExtraFields.CODEC, entityPredicate -> Pair.of(
            entityPredicate.itematic$extraFields(),
            EntityPredicateExtraFields.CODEC
        ), (entityPredicate, extraFields) -> {
            entityPredicate.itematic$setExtraFields(extraFields);
            return entityPredicate;
        }).codec();
    }

    @Inject(
        method = "test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    private void checkForExtraFields(ServerWorld world, Vec3d pos, Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (this.extraFields.usedItemTicks().isPresent() && entity instanceof LivingEntity livingEntity && !this.extraFields.usedItemTicks().get().test(livingEntity.itematic$itemUsedTicks())) {
            info.setReturnValue(false);
            return;
        }

        if (this.extraFields.inWaterOrRain().isPresent() && entity.isTouchingWaterOrRain() != this.extraFields.inWaterOrRain().get()) {
            info.setReturnValue(false);
        }
    }

    @Override
    public EntityPredicateExtraFields itematic$extraFields() {
        return this.extraFields;
    }

    @Override
    public void itematic$setExtraFields(EntityPredicateExtraFields extraFields) {
        this.extraFields = extraFields;
    }

    @Mixin(EntityPredicate.Builder.class)
    public static class BuilderExtender implements EntityPredicateBuilderAccess {
        @Unique
        private NumberRange.IntRange itemUsedTicks;

        @Unique
        private Boolean inWaterOrRain;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private EntityPredicate setExtraFields(EntityPredicate original) {
            EntityPredicateExtraFields extraFields = EntityPredicateExtraFields.of(this.itemUsedTicks, this.inWaterOrRain);
            original.itematic$setExtraFields(extraFields);
            return original;
        }

        @Override
        public EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            this.itemUsedTicks = NumberRange.IntRange.atLeast(ticks);
            return (EntityPredicate.Builder)(Object) this;
        }

        @Override
        public EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            this.inWaterOrRain = inWaterOrRain;
            return (EntityPredicate.Builder)(Object) this;
        }
    }
}
