package net.errorcraft.itematic.mixin.loot.condition;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.access.loot.condition.LocationCheckLootConditionAccess;
import net.errorcraft.itematic.loot.condition.LocationCheckLootConditionExtraFields;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocationCheckLootCondition.class)
public class LocationCheckLootConditionExtender implements LocationCheckLootConditionAccess {
    @Unique
    private LocationCheckLootConditionExtraFields extraFields = LocationCheckLootConditionExtraFields.DEFAULT;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;",
            ordinal = 1,
            remap = false
        )
    )
    private static MapCodec<LocationCheckLootCondition> mapCodecAddExtraFields(MapCodec<LocationCheckLootCondition> original) {
        return original.dependent(
            LocationCheckLootConditionExtraFields.CODEC,
            locationCheck -> Pair.of(
                locationCheck.itematic$extraFields(),
                LocationCheckLootConditionExtraFields.CODEC
            ),
            (locationCheck, extraFields) -> {
                locationCheck.itematic$setExtraFields(extraFields);
                return locationCheck;
            }
        );
    }

    @ModifyArg(
        method = "test(Lnet/minecraft/loot/context/LootContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/context/LootContext;get(Lnet/minecraft/util/context/ContextParameter;)Ljava/lang/Object;"
        )
    )
    private ContextParameter<Vec3d> usePositionTarget(ContextParameter<Vec3d> parameter) {
        return this.extraFields.position().parameter();
    }

    @Override
    public LocationCheckLootConditionExtraFields itematic$extraFields() {
        return this.extraFields;
    }

    @Override
    public void itematic$setExtraFields(LocationCheckLootConditionExtraFields extraFields) {
        this.extraFields = extraFields;
    }
}
