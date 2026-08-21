package net.errorcraft.itematic.mixin.server.level;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerExtender extends Player {
    public ServerPlayerExtender(Level level, GameProfile profile) {
        super(level, profile);
    }

    @WrapMethod(
        method = "openItemGui"
    )
    private void alsoCheckTextHolderItemBehavior(ItemStack stack, InteractionHand hand, Operation<Void> original) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return;
        }

        original.call(stack, hand);
    }

    @WrapMethod(
        method = "awardStat"
    )
    private void checkNullForInvalidStat(@Nullable Stat<?> stat, int count, Operation<Void> original) {
        if (stat == null) {
            return;
        }

        original.call(stat, count);
    }

    @Redirect(
        method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T key, ItemStack stack) {
        return instance.itematic$get(stack.getItemHolder());
    }

    @Redirect(
        method = "onEquippedItemBroken",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T key) {
        Holder<Item> item = this.level()
            .registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .wrapAsHolder((Item) key);
        return instance.itematic$get(item);
    }
}
