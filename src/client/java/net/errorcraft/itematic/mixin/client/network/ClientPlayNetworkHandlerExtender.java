package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.access.network.listener.ClientPlayPacketListenerAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.errorcraft.itematic.world.action.actions.TwirlPlayerAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerExtender extends ClientCommonNetworkHandler implements ClientPlayPacketListenerAccess {
    @Shadow
    private ClientWorld world;

    protected ClientPlayNetworkHandlerExtender(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(
        method = "onSynchronizeTags",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroups;getSearchGroup()Lnet/minecraft/item/ItemGroup;"
        )
    )
    private static void resetItemGroupDisplayContext(CallbackInfo info) {
        ItemGroupsAccessor.setDisplayContext(null);
    }

    @Redirect(
        method = "getActiveDeathProtector",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private static <T> boolean containsDeathProtectionDataComponentUseEventListenerCheck(ItemStack instance, ComponentType<T> type) {
        return instance.itematic$hasEventListener(ItemEvents.BEFORE_DEATH_HOLDER);
    }

    @Redirect(
        method = "getActiveDeathProtector",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForTotemOfUndyingUseCreateStack(ItemConvertible item, PlayerEntity player) {
        return player.getWorld().itematic$createStack(ItemKeys.TOTEM_OF_UNDYING);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void itematic$onTwirl(TwirlS2CPacket packet) {
        TwirlPlayerAction.execute(packet.spinAttackStrength(), this.client.player, this.world, this.client.player.getActiveItem());
    }
}
