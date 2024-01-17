package net.errorcraft.itematic.mixin.village.raid;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raid.class)
public abstract class RaidExtender {
    @Shadow
    public abstract World getWorld();

    @Redirect(
        method = "getOminousBanner",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBannerUseRegistryEntry(ItemConvertible item) {
        return RaidUtil.ominousBanner();
    }

    @Inject(
        method = "getOminousBanner",
        at = @At(
            value = "NEW",
            target = "()Lnet/minecraft/nbt/NbtCompound;"
        ),
        cancellable = true
    )
    private static void checkEmptyStack(CallbackInfoReturnable<ItemStack> info, @Local ItemStack stack) {
        if (stack == ItemStack.EMPTY) {
            info.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Redirect(
        method = "setWaveCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerUseRegistryEntry() {
        return RaidUtil.createOminousBanner(this.getWorld());
    }
}
