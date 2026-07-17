package net.errorcraft.itematic.mixin.loot.condition;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.loot.condition.LocationCheckLootConditionAccess;
import net.errorcraft.itematic.loot.condition.LocationCheckPredicates;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(LocationCheckLootCondition.class)
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
    private static MapCodec<LocationCheckLootCondition> addExtraMapCodecFields(MapCodec<LocationCheckLootCondition> original) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            original.forGetter(Function.identity()),
            PositionTarget.CODEC.optionalFieldOf("position", PositionTarget.ORIGIN).forGetter(LocationCheckLootCondition::itematic$position)
        ).apply(instance, LocationCheckPredicates::setPosition));
    }

    @ModifyArg(
        method = "test(Lnet/minecraft/loot/context/LootContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/context/LootContext;get(Lnet/minecraft/util/context/ContextParameter;)Ljava/lang/Object;"
        )
    )
    private ContextParameter<? extends Vec3d> usePositionTarget(ContextParameter<Vec3d> parameter) {
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
