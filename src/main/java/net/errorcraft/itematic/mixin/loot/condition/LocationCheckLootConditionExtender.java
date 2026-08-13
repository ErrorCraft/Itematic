package net.errorcraft.itematic.mixin.loot.condition;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.loot.condition.LocationCheckLootConditionAccess;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(LocationCheck.class)
public class LocationCheckLootConditionExtender implements LocationCheckLootConditionAccess {
    @Unique
    private PositionTarget position = PositionTarget.ORIGIN;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;",
            ordinal = 1,
            remap = false
        )
    )
    private static MapCodec<LocationCheck> addExtraMapCodecFields(MapCodec<LocationCheck> original) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            original.forGetter(Function.identity()),
            PositionTarget.CODEC.optionalFieldOf("position", PositionTarget.ORIGIN).forGetter(LocationCheck::itematic$position)
        ).apply(instance, LocationCheckPredicates::setPosition));
    }

    @ModifyArg(
        method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootContext;getOptionalParameter(Lnet/minecraft/util/context/ContextKey;)Ljava/lang/Object;"
        )
    )
    private ContextKey<? extends Vec3> usePositionTarget(ContextKey<Vec3> parameter) {
        return this.position.contextParam();
    }

    @Override
    public PositionTarget itematic$position() {
        return this.position;
    }

    @Override
    public void itematic$setPosition(PositionTarget position) {
        this.position = position;
    }
}
