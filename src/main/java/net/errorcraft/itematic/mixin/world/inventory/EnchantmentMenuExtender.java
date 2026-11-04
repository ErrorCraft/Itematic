package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuExtender {
    @WrapOperation(
        method = "lambda$clickMenuButton$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookUseDataComponent(ItemStack instance, Object object, Operation<Boolean> original, @Share("enchantResultItem") LocalRef<Holder<Item>> enchantResultItemReference) {
        Holder<Item> enchantResultItem = instance.get(ItematicDataComponents.ENCHANT_RESULT_ITEM);
        if (enchantResultItem == null) {
            return false;
        }

        enchantResultItemReference.set(enchantResultItem);
        return true;
    }

    @WrapOperation(
        method = "lambda$clickMenuButton$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForEnchantedBookUseDataComponent(ItemStack instance, ItemLike newItem, Operation<ItemStack> original, @Share("enchantResultItem") LocalRef<Holder<Item>> enchantResultItemReference) {
        return instance.itematic$transmuteCopy(enchantResultItemReference.get());
    }

    @WrapOperation(
        method = "getEnchantmentList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookCheckEnchantResultItemDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(ItematicDataComponents.ENCHANT_RESULT_ITEM);
    }

    @WrapOperation(
        method = "quickMoveStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isLapisLazuliCheckTag(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(ItematicItemTags.ENCHANTING_TABLE_INGREDIENTS);
    }

    @Mixin(targets = "net/minecraft/world/inventory/EnchantmentMenu$3")
    public static class LapisLazuliSlotExtender {
        @WrapOperation(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isLapisLazuliCheckTag(ItemStack instance, Object object, Operation<Boolean> original) {
            return instance.is(ItematicItemTags.ENCHANTING_TABLE_INGREDIENTS);
        }
    }
}
