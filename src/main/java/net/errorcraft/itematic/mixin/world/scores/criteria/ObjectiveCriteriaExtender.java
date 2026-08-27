package net.errorcraft.itematic.mixin.world.scores.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.errorcraft.itematic.access.world.scores.criteria.ObjectiveCriteriaAccess;
import net.errorcraft.itematic.resources.RegistryMapperCodec;
import net.errorcraft.itematic.world.scores.criteria.ItematicObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ObjectiveCriteria.class)
public class ObjectiveCriteriaExtender implements ObjectiveCriteriaAccess {
    @Shadow
    @Final
    @Mutable
    private String name;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/PrimitiveCodec;comapFlatMap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<ObjectiveCriteria> useDynamicRegistry(PrimitiveCodec<String> instance, Function<String, DataResult<ObjectiveCriteria>> to, Function<ObjectiveCriteria, String> from) {
        return RegistryMapperCodec.of(
            instance,
            ItematicObjectiveCriteria::byName,
            ObjectiveCriteria::getName
        );
    }

    @Override
    public void itematic$setName(String name) {
        this.name = name;
    }
}
