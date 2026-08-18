package net.errorcraft.itematic.mixin.world.entity.monster.piglin;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinAi.class)
public class PiglinAiExtender {
    @Redirect(
        method = {
            "pickUpItem",
            "wantsToPickup"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isGoldNuggetCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GOLD_NUGGET);
    }

    @Redirect(
        method = "isBarterCurrency",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isGoldIngotCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GOLD_INGOT);
    }

    @Redirect(
        method = "hasCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isHoldingCrossbowCheckId(LivingEntity instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CROSSBOW);
    }
}
