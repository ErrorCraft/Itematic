package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.access.network.protocol.game.ClientGamePacketListenerAccess;
import net.errorcraft.itematic.mixin.world.item.CreativeModeTabsAccessor;
import net.errorcraft.itematic.network.protocol.game.ClientboundTwirlPacket;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.action.actions.TwirlPlayerAction;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerExtender extends ClientCommonPacketListenerImpl implements ClientGamePacketListenerAccess {
    @Shadow
    private ClientLevel level;

    protected ClientPlayNetworkHandlerExtender(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(
        method = "handleUpdateTags",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTabs;searchTab()Lnet/minecraft/world/item/CreativeModeTab;"
        )
    )
    private static void resetItemGroupDisplayContext(CallbackInfo info) {
        CreativeModeTabsAccessor.setCachedParameters(null);
    }

    @Redirect(
        method = "findTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private static <T> boolean containsDeathProtectionDataComponentUseEventListenerCheck(ItemStack instance, DataComponentType<T> type) {
        return instance.itematic$hasEventListener(ItemEvent.BEFORE_DEATH_HOLDER);
    }

    @Redirect(
        method = "findTotem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForTotemOfUndyingUseCreateStack(ItemLike item, Player player) {
        return player.level().itematic$createStack(ItemIds.TOTEM_OF_UNDYING);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void itematic$handleTwirl(ClientboundTwirlPacket packet) {
        TwirlPlayerAction.execute(packet.spinAttackStrength(), this.minecraft.player, this.level, this.minecraft.player.getUseItem());
    }
}
