package net.errorcraft.itematic.mixin.predicate.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.predicate.EntityPredicateAccess;
import net.errorcraft.itematic.predicate.EntityPredicateExtraFields;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
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
        method = "matches(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    private void checkForExtraFields(ServerLevel world, Vec3 pos, Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (this.extraFields.usedItemTicks().isPresent() && entity instanceof LivingEntity livingEntity && !this.extraFields.usedItemTicks().get().matches(livingEntity.itematic$itemUsedTicks())) {
            info.setReturnValue(false);
        }
        if (this.extraFields.inWaterOrRain().isPresent() && entity.isInWaterOrRain() != this.extraFields.inWaterOrRain().get()) {
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
    public static class BuilderExtender implements BuilderAccess {
        @Unique
        private MinMaxBounds.Ints itemUsedTicks;
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
            this.itemUsedTicks = MinMaxBounds.Ints.atLeast(ticks);
            return (EntityPredicate.Builder)(Object) this;
        }

        @Override
        public EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            this.inWaterOrRain = inWaterOrRain;
            return (EntityPredicate.Builder)(Object) this;
        }
    }
}
