package net.errorcraft.itematic.mixin.world.item.component;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.function.Function;

@Mixin(Consumable.class)
public class ConsumableExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Function<RecordCodecBuilder.Instance<Consumable>, ? extends App<RecordCodecBuilder.Mu<Consumable>, Consumable>> doNotUseAllFields(Function<RecordCodecBuilder.Instance<Consumable>, ? extends App<RecordCodecBuilder.Mu<Consumable>, Consumable>> builder) {
        return instance -> instance.group(
            SoundEvent.CODEC.optionalFieldOf("sound", SoundEvents.GENERIC_EAT).forGetter(Consumable::sound),
            Codec.BOOL.optionalFieldOf("has_consume_particles", true).forGetter(Consumable::hasConsumeParticles)
        ).apply(instance, (sound, hasConsumeParticles) -> new Consumable(0.0f, ItemUseAnimation.NONE, sound, hasConsumeParticles, List.of()));
    }
}
