package net.errorcraft.itematic.mixin.scoreboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.errorcraft.itematic.access.scoreboard.ScoreboardCriterionAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.errorcraft.itematic.serialization.RegistryMapperCodec;
import net.minecraft.scoreboard.ScoreboardCriterion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ScoreboardCriterion.class)
public class ScoreboardCriterionExtender implements ScoreboardCriterionAccess {
    @Shadow
    @Final
    @Mutable
    private String name;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/PrimitiveCodec;comapFlatMap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<ScoreboardCriterion> useDynamicRegistry(PrimitiveCodec<String> instance, Function<String, DataResult<ScoreboardCriterion>> to, Function<ScoreboardCriterion, String> from) {
        return RegistryMapperCodec.of(
            instance,
            ScoreboardCriterionUtil::byName,
            ScoreboardCriterion::getName
        );
    }

    @Override
    public void itematic$setName(String name) {
        this.name = name;
    }
}
