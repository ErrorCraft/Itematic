package net.errorcraft.itematic.mixin.world.entity.raid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidExtender {
    @WrapOperation(
        method = "getOminousBannerInstance",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWhiteBannerUseHolder(ItemLike item, Operation<ItemStack> original, @Cancellable CallbackInfoReturnable<ItemStack> info) {
        ItemStack stack = ItematicRaids.ominousBanner();
        if (stack.isEmpty()) {
            info.setReturnValue(ItemStack.EMPTY);
        }

        return stack;
    }

    @Inject(
        method = "setLeader",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void setOminousBannerForLaterUse(int wave, Raider raider, CallbackInfo info) {
        ItematicRaids.createOminousBanner(raider.level());
    }
}
