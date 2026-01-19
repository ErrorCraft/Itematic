package net.errorcraft.itematic.item.shooter;

import net.minecraft.item.CrossbowItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.sound.SoundEvent;

import java.util.Optional;

public class ChargingSoundsUtil {
    public static final CrossbowItem.LoadingSounds EMPTY = new CrossbowItem.LoadingSounds(Optional.empty(), Optional.empty(), Optional.empty());
    public static final PacketCodec<RegistryByteBuf, CrossbowItem.LoadingSounds> PACKET_CODEC = PacketCodec.tuple(
        SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), CrossbowItem.LoadingSounds::start,
        SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), CrossbowItem.LoadingSounds::mid,
        SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), CrossbowItem.LoadingSounds::end,
        CrossbowItem.LoadingSounds::new
    );

    private ChargingSoundsUtil() {}
}
