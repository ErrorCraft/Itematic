package net.errorcraft.itematic.mixin.screen;

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
public class EnchantmentScreenHandlerExtender {
    @Redirect(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemBehavior(ItemStack instance, Item item, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        Optional<Holder<Item>> optionalItem = instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto);
        optionalItem.ifPresent(transformsInto::set);
        return optionalItem.isPresent();
    }

    @Redirect(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack withItemForEnchantedBookUseItemBehavior(ItemStack instance, ItemLike item, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        return instance.itematic$copyWithItem(transformsInto.get());
    }

    @Redirect(
        method = "getEnchantmentList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemBehavior(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto)
            .isPresent();
    }

    @Redirect(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.LAPIS_LAZULI);
    }

    @Mixin(targets = "net/minecraft/world/inventory/EnchantmentMenu$3")
    public static class LapisLazuliSlotExtender {
        @Redirect(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
        )
        private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemIds.LAPIS_LAZULI);
        }
    }
}
