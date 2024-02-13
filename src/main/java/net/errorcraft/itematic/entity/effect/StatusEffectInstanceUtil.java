package net.errorcraft.itematic.entity.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

public class StatusEffectInstanceUtil {
    public static final Codec<StatusEffectInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.STATUS_EFFECT.getEntryCodec().fieldOf("id").forGetter(StatusEffectInstance::getEffectType),
        Codecs.createStrictOptionalFieldCodec(Codec.INT, "duration", 0).forGetter(StatusEffectInstance::getDuration),
        Codecs.createStrictOptionalFieldCodec(Codecs.UNSIGNED_BYTE, "amplifier", 0).forGetter(StatusEffectInstance::getAmplifier)
    ).apply(instance, StatusEffectInstance::new));

    private StatusEffectInstanceUtil() {}
}
