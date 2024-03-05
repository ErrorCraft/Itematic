package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
public abstract class ItemEntityExtender {
    @Redirect(
        method = "damage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForNetherStarReturnFalse(ItemStack instance, Item item) {
        return false;
    }

    @Redirect(
        method = "onPlayerCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }
}
