package net.errorcraft.itematic.mixin.client.recipebook;

import net.errorcraft.itematic.access.client.recipebook.RecipeBookWidgetTabAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeBookGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

public class RecipeBookWidgetExtender {
    @Mixin(RecipeBookWidget.Tab.class)
    public static class TabExtender implements RecipeBookWidgetTabAccess {
        @Unique
        private RegistryKey<Item> primaryIconItem;
        @Unique
        private Optional<RegistryKey<Item>> secondaryIconItem;

        @Redirect(
            method = {
                "<init>(Lnet/minecraft/client/recipebook/RecipeBookType;)V",
                "<init>(Lnet/minecraft/item/Item;Lnet/minecraft/recipe/book/RecipeBookGroup;)V",
                "<init>(Lnet/minecraft/item/Item;Lnet/minecraft/item/Item;Lnet/minecraft/recipe/book/RecipeBookGroup;)V"
            },
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackUseEmptyItemStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }

        @Inject(
            method = "<init>(Lnet/minecraft/client/recipebook/RecipeBookType;)V",
            at = @At("TAIL")
        )
        private void setIcons(RecipeBookType type, CallbackInfo info) {
            this.primaryIconItem = ItemKeys.COMPASS;
            this.secondaryIconItem = Optional.empty();
        }

        @Inject(
            method = "<init>(Lnet/minecraft/item/Item;Lnet/minecraft/recipe/book/RecipeBookGroup;)V",
            at = @At("TAIL")
        )
        private void setIcons(Item primaryIcon, RecipeBookGroup group, CallbackInfo info) {
            this.primaryIconItem = Registries.ITEM.getKey(primaryIcon).orElseThrow();
            this.secondaryIconItem = Optional.empty();
        }

        @Inject(
            method = "<init>(Lnet/minecraft/item/Item;Lnet/minecraft/item/Item;Lnet/minecraft/recipe/book/RecipeBookGroup;)V",
            at = @At("TAIL")
        )
        private void setIcons(Item primaryIcon, Item secondaryIcon, RecipeBookGroup group, CallbackInfo info) {
            this.primaryIconItem = Registries.ITEM.getKey(primaryIcon).orElseThrow();
            this.secondaryIconItem = Optional.of(Registries.ITEM.getKey(secondaryIcon).orElseThrow());
        }

        @Override
        public ItemStack itematic$primaryIconItem(ItemAccess items) {
            return items.getOptionalEntry(this.primaryIconItem)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }

        @Override
        public Optional<ItemStack> itematic$secondaryIconItem(ItemAccess items) {
            return this.secondaryIconItem.flatMap(items::getOptionalEntry)
                .map(ItemStack::new);
        }
    }
}
