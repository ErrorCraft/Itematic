package net.errorcraft.itematic.mixin.component.type;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.function.Function;

@Mixin(ConsumableComponent.class)
public class ConsumableComponentExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Function<RecordCodecBuilder.Instance<ConsumableComponent>, ? extends App<RecordCodecBuilder.Mu<ConsumableComponent>, ConsumableComponent>> createDoNotUseAllFields(Function<RecordCodecBuilder.Instance<ConsumableComponent>, ? extends App<RecordCodecBuilder.Mu<ConsumableComponent>, ConsumableComponent>> builder) {
        return instance -> instance.group(
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound", SoundEvents.ENTITY_GENERIC_EAT).forGetter(ConsumableComponent::sound),
            Codec.BOOL.optionalFieldOf("has_consume_particles", true).forGetter(ConsumableComponent::hasConsumeParticles)
        ).apply(instance, (sound, hasConsumeParticles) -> new ConsumableComponent(0.0f, UseAction.NONE, sound, hasConsumeParticles, List.of()));
    }
}
