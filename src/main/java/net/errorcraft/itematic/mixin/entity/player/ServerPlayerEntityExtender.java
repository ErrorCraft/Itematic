package net.errorcraft.itematic.mixin.entity.player;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityExtender extends Player {
    public ServerPlayerEntityExtender(Level world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
        method = "openItemGui",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, InteractionHand hand, CallbackInfo info) {
        if (!book.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            info.cancel();
        }
    }

    @Inject(
        method = "awardStat",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkNullForInvalidStat(Stat<?> stat, int amount, CallbackInfo info) {
        if (stat == null) {
            info.cancel();
        }
    }

    @Redirect(
        method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getItemHolder());
    }

    @Redirect(
        method = "onEquippedItemBroken",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key) {
        Holder<Item> itemEntry = this.level()
            .registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .wrapAsHolder((Item) key);
        return instance.itematic$getOrCreateStat(itemEntry);
    }
}
