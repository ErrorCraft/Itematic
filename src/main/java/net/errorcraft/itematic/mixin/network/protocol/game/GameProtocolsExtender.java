package net.errorcraft.itematic.mixin.network.protocol.game;

import net.errorcraft.itematic.network.protocol.game.ClientboundTwirlPacket;
import net.errorcraft.itematic.network.protocol.game.ItematicGamePacketTypes;
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
        method = "lambda$static$2",
        at = @At("TAIL")
    )
    private static void registerCustomClientboundPackets(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf, Unit> builder, CallbackInfo info) {
        builder.addPacket(ItematicGamePacketTypes.TWIRL, ClientboundTwirlPacket.STREAM_CODEC);
    }
}
