package net.errorcraft.itematic.mixin.village.raid;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidExtender {
    @Redirect(
        method = "getOminousBannerInstance",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBannerUseRegistryEntry(ItemLike item) {
        return RaidUtil.ominousBanner();
    }

    @Inject(
        method = "getOminousBannerInstance",
        at = @At(
            value = "NEW",
            target = "()Lnet/minecraft/world/level/block/entity/BannerPatternLayers$Builder;"
        ),
        cancellable = true
    )
    private static void checkEmptyStack(CallbackInfoReturnable<ItemStack> info, @Local ItemStack stack) {
        if (stack == ItemStack.EMPTY) {
            info.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(
        method = "setLeader",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(int wave, Raider entity, CallbackInfo info) {
        RaidUtil.createOminousBanner(entity.level());
    }
}
