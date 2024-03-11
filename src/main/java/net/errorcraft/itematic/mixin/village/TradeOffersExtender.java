package net.errorcraft.itematic.mixin.village;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TradeOffers.class)
public class TradeOffersExtender {
    @Redirect(
        method = "createPotionStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/PotionContentsComponent;createStack(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack createStackForPotionReturnEmptyStack(Item item, RegistryEntry<Potion> potion) {
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "enchant",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "enchant",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V"
        )
    )
    private static void doNotAddEnchantment(ItemStack instance, Enchantment enchantment, int level) {}
}
