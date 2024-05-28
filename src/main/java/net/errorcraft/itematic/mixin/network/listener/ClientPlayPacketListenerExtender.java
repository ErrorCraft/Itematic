package net.errorcraft.itematic.mixin.network.listener;

import net.errorcraft.itematic.access.network.listener.ClientPlayPacketListenerAccess;
import net.minecraft.network.listener.ClientPlayPacketListener;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayPacketListener.class)
public interface ClientPlayPacketListenerExtender extends ClientPlayPacketListenerAccess {
}
