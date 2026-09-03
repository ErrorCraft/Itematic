package net.errorcraft.itematic.mixin.server.commands;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantCommand.class)
public class EnchantCommandExtender {
    @WrapOperation(
        method = "enchant",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
        )
    )
    private static boolean isEmptyCheckInteractableStack(ItemStack instance, Operation<Boolean> original) {
        return instance.itematic$cannotBeInteractedWith();
    }
}
