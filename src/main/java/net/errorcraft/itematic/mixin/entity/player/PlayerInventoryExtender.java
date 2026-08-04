package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Inventory.class)
public class PlayerInventoryExtender {
    @Redirect(
        method = "add(ILnet/minecraft/world/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getId(Lnet/minecraft/world/item/Item;)I"
        )
    )
    private int getRawIdReturnZero(Item item) {
        return 0;
    }

    @ModifyArg(
        method = "add(ILnet/minecraft/world/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/CrashReportCategory;setDetail(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;",
            ordinal = 0
        )
    )
    private Object addItemIdToCrashReportUseRegistryKey(Object detail, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$key().identifier();
    }
}
