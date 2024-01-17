package net.errorcraft.itematic.mixin.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerExtender {
    @Redirect(
        method = "method_17410",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemComponent(ItemStack instance, Item item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        Optional<RegistryEntry<Item>> optionalEntry = instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        optionalEntry.ifPresent(transformsInto::set);
        return optionalEntry.isPresent();
    }

    @Redirect(
        method = "method_17410",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForEnchantedBookUseItemComponent(ItemConvertible item, @Share("transformsInto") LocalRef<RegistryEntry<Item>> transformsInto) {
        return new ItemStack(transformsInto.get());
    }

    @Redirect(
        method = "generateEnchantments",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBookUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto)
            .isPresent();
    }

    @Redirect(
        method = "quickMove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.LAPIS_LAZULI);
    }

    @Mixin(targets = "net/minecraft/screen/EnchantmentScreenHandler$3")
    public static class LapisLazuliSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForLapisLazuliUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.LAPIS_LAZULI);
        }
    }
}
