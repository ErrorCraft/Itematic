package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public class PlayerInventoryExtender {
    @Redirect(
        method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getRawId(Lnet/minecraft/item/Item;)I"
        )
    )
    private int getRawIdReturnZero(Item item) {
        return 0;
    }

    @ModifyArg(
        method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/crash/CrashReportSection;add(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/util/crash/CrashReportSection;",
            ordinal = 0
        )
    )
    private Object addItemIdToCrashReportUseRegistryKey(Object detail, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$key().getValue();
    }
}
