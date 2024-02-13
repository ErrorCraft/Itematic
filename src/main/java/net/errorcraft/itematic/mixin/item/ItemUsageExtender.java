package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.errorcraft.itematic.item.ItemUsageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemUsage.class)
public class ItemUsageExtender {
    @WrapWithCondition(
        method = "exchangeStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private static boolean checkDecrementStackCount(ItemStack instance, int amount, LivingEntity entity) {
        return ItemUsageUtil.decrementCount();
    }
}
