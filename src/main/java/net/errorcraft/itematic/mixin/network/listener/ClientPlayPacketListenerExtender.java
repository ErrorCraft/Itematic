package net.errorcraft.itematic.mixin.network.listener;

import net.errorcraft.itematic.access.network.listener.ClientPlayPacketListenerAccess;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientGamePacketListener.class)
public interface ClientPlayPacketListenerExtender extends ClientPlayPacketListenerAccess {
}
