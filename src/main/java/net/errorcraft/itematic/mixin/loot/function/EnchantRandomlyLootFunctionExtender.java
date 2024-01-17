package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.enchantment.Enchantment;
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
        method = "method_53327",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private static boolean isValidEnchantUseItemComponent(Enchantment instance, ItemStack stack, @Local(argsOnly = true) RegistryEntry.Reference<Enchantment> enchantment) {
        return stack.itematic$getComponent(ItemComponentTypes.ENCHANTABLE).map(c -> c.enchantments()
            .map(enchantment::isIn)
            .orElse(true)
        ).orElse(false);
    }

    @Redirect(
        method = "addEnchantmentToStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForBookUseItemComponent(ItemStack instance, Item item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        Optional<RegistryEntry<Item>> optionalEntry = instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        optionalEntry.ifPresent(transformsInto::set);
        return optionalEntry.isPresent();
    }

    @Redirect(
        method = "addEnchantmentToStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForEnchantedBookUseItemComponent(ItemConvertible item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        return new ItemStack(transformsInto.get());
    }
}
