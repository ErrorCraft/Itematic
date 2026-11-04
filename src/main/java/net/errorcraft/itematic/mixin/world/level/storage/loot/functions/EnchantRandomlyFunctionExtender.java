package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionExtender {
    @WrapOperation(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookCheckEnchantResultItemDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(ItematicDataComponents.ENCHANT_RESULT_ITEM);
    }

    @WrapOperation(
        method = "enchantItem",
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
        method = "enchantItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseDataComponent(ItemLike item, Operation<ItemStack> original, ItemStack itemStack, @Share("enchantResultItem") LocalRef<Holder<Item>> enchantResultItemReference) {
        return itemStack.itematic$transmuteCopy(enchantResultItemReference.get());
    }
}
