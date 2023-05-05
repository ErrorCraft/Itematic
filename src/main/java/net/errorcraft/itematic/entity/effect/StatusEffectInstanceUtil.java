package net.errorcraft.itematic.entity.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;

public class StatusEffectInstanceUtil {
    public static final Codec<StatusEffectInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.STATUS_EFFECT.getCodec().fieldOf("id").forGetter(StatusEffectInstance::getEffectType),
        Codec.INT.fieldOf("duration").forGetter(StatusEffectInstance::getDuration),
        Codec.INT.fieldOf("amplifier").forGetter(StatusEffectInstance::getAmplifier)
    ).apply(instance, StatusEffectInstance::new));

    private StatusEffectInstanceUtil() {}
}
