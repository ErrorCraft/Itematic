package net.errorcraft.itematic.mixin.village;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerTrades.class)
public class TradeOffersExtender {
    @Redirect(
        method = "method_16929",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackReturnEmptyStack(ItemLike item) {
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "potion",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/alchemy/PotionContents;createItemStack(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack createStackForPotionReturnEmptyStack(Item item, Holder<Potion> potion) {
        return ItemStack.EMPTY;
    }

    @Mixin(VillagerTrades.ItemsForEmeralds.class)
    public static class SellItemFactoryExtender {
        @Redirect(
            method = {
                "<init>(Lnet/minecraft/world/level/block/Block;IIII)V",
                "<init>(Lnet/minecraft/world/item/Item;III)V",
                "<init>(Lnet/minecraft/world/item/Item;IIII)V",
                "<init>(Lnet/minecraft/world/item/Item;IIIIF)V",
                "<init>(Lnet/minecraft/world/item/Item;IIIIFLnet/minecraft/resources/ResourceKey;)V"
            },
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(VillagerTrades.ItemsAndEmeraldsToItems.class)
    public static class ProcessItemFactoryExtender {
        @Redirect(
            method = "<init>(Lnet/minecraft/world/level/ItemLike;IILnet/minecraft/world/item/Item;IIIF)V",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }

        @Redirect(
            method = "<init>(Lnet/minecraft/world/level/ItemLike;IILnet/minecraft/world/level/ItemLike;IIIFLnet/minecraft/resources/ResourceKey;)V",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemLike item, int count) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(VillagerTrades.EnchantedItemForEmeralds.class)
    public static class SellEnchantedToolFactoryExtender {
        @Redirect(
            method = "<init>(Lnet/minecraft/world/item/Item;IIIF)V",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackReturnEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }
    }

    @Mixin(VillagerTrades.TippedArrowForItemsAndEmeralds.class)
    public static class SellPotionHoldingItemFactoryExtender {
        @Redirect(
            method = "<init>",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackReturnEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }
    }
}
