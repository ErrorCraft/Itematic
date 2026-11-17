package net.errorcraft.itematic.mixin.world.item.enchantment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperExtender {
    @WrapOperation(
        method = "getComponentType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isEnchantedBookCheckStoredEnchantmentsDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(DataComponents.STORED_ENCHANTMENTS);
    }

    @WrapOperation(
        method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBookUseDataComponent(ItemStack instance, Object object, Operation<Boolean> original, @Share("enchantResultItem") LocalRef<Holder<Item>> enchantResultItemReference) {
        Holder<Item> enchantResultItem = instance.get(ItematicDataComponents.ENCHANT_RESULT_ITEM);
        if (enchantResultItem == null) {
            return false;
        }

        enchantResultItemReference.set(enchantResultItem);
        return true;
    }

    @WrapOperation(
        method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseDataComponent(ItemLike item, Operation<ItemStack> original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack, @Share("enchantResultItem") LocalRef<Holder<Item>> enchantResultItemReference) {
        return itemStack.itematic$transmuteCopy(enchantResultItemReference.get());
    }

    @WrapOperation(
        method = "getAvailableEnchantmentResults",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBookCheckEnchantResultItemDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(ItematicDataComponents.ENCHANT_RESULT_ITEM);
    }
}
