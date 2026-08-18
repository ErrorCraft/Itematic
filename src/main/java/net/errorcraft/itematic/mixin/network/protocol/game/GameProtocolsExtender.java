package net.errorcraft.itematic.mixin.network.protocol.game;

import net.errorcraft.itematic.network.packet.ItematicPlayPackets;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameProtocols.class)
public class GameProtocolsExtender {
    @Inject(
        method = "method_55958",
        at = @At("TAIL")
    )
    private static void registerCustomClientboundPackets(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf, Unit> builder, CallbackInfo info) {
        builder.addPacket(ItematicPlayPackets.TWIRL, TwirlS2CPacket.CODEC);
    }
}
