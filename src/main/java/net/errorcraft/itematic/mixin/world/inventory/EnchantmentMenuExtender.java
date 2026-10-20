package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.errorcraft.itematic.world.item.enchantment.Enchantables;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuExtender {
    @Redirect(
        method = "lambda$clickMenuButton$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookUseDataComponent(ItemStack instance, Object o, @Share("transformsInto") LocalRef<Holder<Item>> transformsIntoReference) {
        Optional<Holder<Item>> transformsInto = Enchantables.transformsInto(instance);
        transformsInto.ifPresent(transformsIntoReference::set);
        return transformsInto.isPresent();
    }

    @Redirect(
        method = "lambda$clickMenuButton$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForEnchantedBookUseDataComponent(ItemStack instance, ItemLike newItem, @Share("transformsInto") LocalRef<Holder<Item>> transformsIntoReference) {
        return instance.itematic$transmuteCopy(transformsIntoReference.get());
    }

    @Redirect(
        method = "getEnchantmentList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookUseDataComponent(ItemStack instance, Object o) {
        return Enchantables.transformsInto(instance).isPresent();
    }

    @Redirect(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isLapisLazuliCheckTag(ItemStack instance, Object o) {
        return instance.is(ItematicItemTags.ENCHANTING_TABLE_INGREDIENTS);
    }

    @Mixin(targets = "net/minecraft/world/inventory/EnchantmentMenu$3")
    public static class LapisLazuliSlotExtender {
        @Redirect(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isLapisLazuliCheckTag(ItemStack instance, Object o) {
            return instance.is(ItematicItemTags.ENCHANTING_TABLE_INGREDIENTS);
        }
    }
}
