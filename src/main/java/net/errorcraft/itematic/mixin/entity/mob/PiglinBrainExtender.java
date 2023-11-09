package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinBrain.class)
public class PiglinBrainExtender {
    @Redirect(
        method = { "loot", "canGather" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForGoldNuggetUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GOLD_NUGGET);
    }

    @Redirect(
        method = "isHoldingCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isHoldingForCrossbowUseRegistryKeyCheck(LivingEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
    }
}
