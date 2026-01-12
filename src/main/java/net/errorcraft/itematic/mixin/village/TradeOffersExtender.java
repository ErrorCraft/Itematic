package net.errorcraft.itematic.mixin.village;

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
        method = "method_16929",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }

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

    @Mixin(TradeOffers.SellItemFactory.class)
    public static class SellItemFactoryExtender {
        @Redirect(
            method = { "<init>(Lnet/minecraft/block/Block;IIII)V", "<init>(Lnet/minecraft/item/Item;III)V", "<init>(Lnet/minecraft/item/Item;IIII)V", "<init>(Lnet/minecraft/item/Item;IIIIF)V" },
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(TradeOffers.ProcessItemFactory.class)
    public static class ProcessItemFactoryExtender {
        @Redirect(
            method = "<init>(Lnet/minecraft/item/ItemConvertible;IILnet/minecraft/item/Item;IIIF)V",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(TradeOffers.SellEnchantedToolFactory.class)
    public static class SellEnchantedToolFactoryExtender {
        @Redirect(
            method = "<init>(Lnet/minecraft/item/Item;IIIF)V",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(TradeOffers.SellPotionHoldingItemFactory.class)
    public static class SellPotionHoldingItemFactoryExtender {
        @Redirect(
            method = "<init>",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }
    }
}
