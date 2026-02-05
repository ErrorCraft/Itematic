package net.errorcraft.itematic.mixin.entity.player;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityExtender extends PlayerEntity {
    public ServerPlayerEntityExtender(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
        method = "useBook",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, Hand hand, CallbackInfo info) {
        if (!book.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER)) {
            info.cancel();
        }
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
            .getOrThrow(RegistryKeys.ITEM)
            .getEntry((Item) key);
        return instance.itematic$getOrCreateStat(itemEntry);
    }
}
