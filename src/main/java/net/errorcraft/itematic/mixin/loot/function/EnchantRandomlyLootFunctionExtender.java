package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionExtender {
    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto)
            .isPresent();
    }

    @Redirect(
        method = "addEnchantmentToStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForBookUseItemComponentStatic(ItemStack instance, Item item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        Optional<RegistryEntry<Item>> optionalItem = instance.itematic$getBehavior(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        optionalItem.ifPresent(transformsInto::set);
        return optionalItem.isPresent();
    }

    @Redirect(
        method = "addEnchantmentToStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseItemComponent(ItemConvertible item, ItemStack stack, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        return stack.itematic$copyWithItem(transformsInto.get());
    }
}
