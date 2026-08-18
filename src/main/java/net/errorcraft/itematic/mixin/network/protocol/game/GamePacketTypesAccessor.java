package net.errorcraft.itematic.mixin.network.protocol.game;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.GamePacketTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GamePacketTypes.class)
public interface GamePacketTypesAccessor {
    @Invoker("createClientbound")
    static <T extends Packet<ClientGamePacketListener>> PacketType<T> createClientbound(String id) {
        throw new AssertionError();
    }
}
