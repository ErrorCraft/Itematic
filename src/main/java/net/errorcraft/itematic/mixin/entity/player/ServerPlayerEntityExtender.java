package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.access.entity.LivingEntityAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetworkSyncedItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityExtender extends PlayerEntity implements LivingEntityAccess {
    public ServerPlayerEntityExtender(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Redirect(
        method = "playerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "playerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;isNetworkSynced()Z"
        )
    )
    private boolean isNetworkSyncedUseItemStackVersion(Item instance, @Local ItemStack stack) {
        return stack.itematic$isNetworkSynced();
    }

    @Redirect(
        method = "playerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/NetworkSyncedItem;createSyncPacket(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/network/packet/Packet;"
        )
    )
    @Nullable
    private Packet<?> createSyncPacketUseItemComponent(NetworkSyncedItem instance, ItemStack stack, World world, PlayerEntity player) {
        return stack.itematic$getComponent(ItemComponentTypes.MAP_HOLDER)
            .map(c -> c.createSyncPacket(stack, world, player))
            .orElse(null);
    }

    @Redirect(
        method = "useBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWrittenBookUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER);
    }

    @Inject(
        method = "increaseStat",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkNullForInvalidStat(Stat<?> stat, int amount, CallbackInfo info) {
        if (stat == null) {
            info.cancel();
        }
    }

    @Redirect(
        method = "dropItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }

    @Redirect(
        method = "sendEquipmentBreakStatus",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key) {
        RegistryEntry<Item> itemEntry = this.getWorld()
            .getRegistryManager()
            .get(RegistryKeys.ITEM)
            .getEntry((Item) key);
        return instance.itematic$getOrCreateStat(itemEntry);
    }

    @Override
    public void itematic$addOrDropStack(ItemStack stack) {
        if (!this.getInventory().insertStack(stack)) {
            this.dropStack(stack);
        }
    }
}
