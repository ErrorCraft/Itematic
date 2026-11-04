package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ZoomFieldOfViewMultiplier(float multiplier) {
    public static final Codec<ZoomFieldOfViewMultiplier> CODEC = ItematicCodecs.positiveFloat(1.0f).xmap(
        ZoomFieldOfViewMultiplier::new,
        ZoomFieldOfViewMultiplier::multiplier
    );
    public static final StreamCodec<ByteBuf, ZoomFieldOfViewMultiplier> STREAM_CODEC = ByteBufCodecs.FLOAT.map(
        ZoomFieldOfViewMultiplier::new,
        ZoomFieldOfViewMultiplier::multiplier
    );
}
