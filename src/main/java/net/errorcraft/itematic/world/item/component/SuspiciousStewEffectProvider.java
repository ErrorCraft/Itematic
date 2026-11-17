package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

public record SuspiciousStewEffectProvider(SuspiciousStewEffects effects) implements SuspiciousEffectHolder {
    public static final Codec<SuspiciousStewEffectProvider> CODEC = SuspiciousStewEffects.CODEC.xmap(
        SuspiciousStewEffectProvider::new,
        SuspiciousStewEffectProvider::effects
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousStewEffectProvider> STREAM_CODEC = SuspiciousStewEffects.STREAM_CODEC.map(
        SuspiciousStewEffectProvider::new,
        SuspiciousStewEffectProvider::effects
    );

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return this.effects;
    }
}
