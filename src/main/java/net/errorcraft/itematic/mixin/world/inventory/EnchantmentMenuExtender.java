package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
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
    private boolean isBookUseItemBehavior(ItemStack instance, Object o, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        Optional<Holder<Item>> item = instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto);
        item.ifPresent(transformsInto::set);
        return item.isPresent();
    }

    @Redirect(
        method = "lambda$clickMenuButton$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForEnchantedBookUseHolder(ItemStack instance, ItemLike newItem, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        return instance.itematic$transmuteCopy(transformsInto.get());
    }

    @Redirect(
        method = "getEnchantmentList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookUseItemBehavior(ItemStack instance, Object o) {
        return instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto)
            .isPresent();
    }

    @Redirect(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isLapisLazuliCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.LAPIS_LAZULI);
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
        private boolean isLapisLazuliCheckId(ItemStack instance, Object o) {
            return instance.is(ItemIds.LAPIS_LAZULI);
        }
    }
}
