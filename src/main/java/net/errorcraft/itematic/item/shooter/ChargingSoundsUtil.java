package net.errorcraft.itematic.item.shooter;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CrossbowItem;

import java.util.Optional;

public class ChargingSoundsUtil {
    public static final CrossbowItem.ChargingSounds EMPTY = new CrossbowItem.ChargingSounds(Optional.empty(), Optional.empty(), Optional.empty());
    public static final StreamCodec<RegistryFriendlyByteBuf, CrossbowItem.ChargingSounds> PACKET_CODEC = StreamCodec.composite(
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), CrossbowItem.ChargingSounds::start,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), CrossbowItem.ChargingSounds::mid,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), CrossbowItem.ChargingSounds::end,
        CrossbowItem.ChargingSounds::new
    );

    private ChargingSoundsUtil() {}
}
