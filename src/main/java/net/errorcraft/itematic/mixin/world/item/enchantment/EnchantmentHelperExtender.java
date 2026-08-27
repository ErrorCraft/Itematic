package net.errorcraft.itematic.mixin.world.item.enchantment;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperExtender {
    @Redirect(
        method = "getComponentType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isEnchantedBookCheckEnchantmentHolderItemBehavior(ItemStack instance, Object o) {
        return instance.itematic$hasBehavior(ItemBehaviorType.ENCHANTMENT_HOLDER);
    }

    @Redirect(
        method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBookUseItemBehavior(ItemStack instance, Object o, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        Optional<Holder<Item>> item = instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto);
        item.ifPresent(transformsInto::set);
        return item.isPresent();
    }

    @Redirect(
        method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseHolder(ItemLike item, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack, @Share("transformsInto") LocalRef<Holder<Item>> transformsInto) {
        return itemStack.itematic$transmuteCopy(transformsInto.get());
    }

    @Redirect(
        method = "getAvailableEnchantmentResults",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBookUseItemBehavior(ItemStack instance, Object o) {
        return instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto)
            .isPresent();
    }
}
