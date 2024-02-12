package net.errorcraft.itematic.mixin.network.state;

import net.errorcraft.itematic.network.packet.ItematicPlayPackets;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.minecraft.network.NetworkStateBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.state.PlayStateFactories;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayStateFactories.class)
public class PlayStateFactoriesExtender {
    @Inject(
        method = "method_55958",
        at = @At("TAIL")
    )
    private static void registerCustomS2CPackets(NetworkStateBuilder<ClientPlayPacketListener, RegistryByteBuf> builder, CallbackInfo info) {
        builder.add(ItematicPlayPackets.TWIRL, TwirlS2CPacket.CODEC);
    }
}
