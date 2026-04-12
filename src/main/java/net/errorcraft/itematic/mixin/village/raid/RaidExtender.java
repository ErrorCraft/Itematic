package net.errorcraft.itematic.mixin.village.raid;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidExtender {
    @Redirect(
        method = "createOminousBanner",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBannerUseRegistryEntry(ItemConvertible item) {
        return RaidUtil.ominousBanner();
    }

    @Inject(
        method = "createOminousBanner",
        at = @At(
            value = "NEW",
            target = "()Lnet/minecraft/component/type/BannerPatternsComponent$Builder;"
        ),
        cancellable = true
    )
    private static void checkEmptyStack(CallbackInfoReturnable<ItemStack> info, @Local ItemStack stack) {
        if (stack == ItemStack.EMPTY) {
            info.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(
        method = "setWaveCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;createOminousBanner(Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(int wave, RaiderEntity entity, CallbackInfo info) {
        RaidUtil.createOminousBanner(entity.getWorld());
    }
}
