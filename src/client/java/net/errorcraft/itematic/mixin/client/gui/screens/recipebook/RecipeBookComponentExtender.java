package net.errorcraft.itematic.mixin.client.gui.screens.recipebook;

import net.errorcraft.itematic.access.client.gui.screens.recipebook.RecipeBookComponentAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(RecipeBookComponent.class)
public class RecipeBookComponentExtender {
    @Shadow
    @Final
    private StackedItemContents stackedContents;

    @Inject(
        method = "init",
        at = @At("HEAD")
    )
    @SuppressWarnings("DataFlowIssue")
    private void setStackedContentsLevel(int width, int height, Minecraft minecraft, boolean widthTooNarrow, CallbackInfo info) {
        this.stackedContents.itematic$setLevel(minecraft.level);
    }

    @Mixin(RecipeBookComponent.TabInfo.class)
    public static class TabInfoExtender implements RecipeBookComponentAccess.TabInfoAccess {
        @Unique
        private ResourceKey<Item> primaryIconItem;
        @Unique
        private Optional<ResourceKey<Item>> secondaryIconItem;

        @Redirect(
            method = {
                "<init>(Lnet/minecraft/client/gui/screens/recipebook/SearchRecipeBookCategory;)V",
                "<init>(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/crafting/RecipeBookCategory;)V",
                "<init>(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/crafting/RecipeBookCategory;)V"
            },
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackUseEmptyItemStack(ItemLike item) {
            return ItemStack.EMPTY;
        }

        @Inject(
            method = "<init>(Lnet/minecraft/client/gui/screens/recipebook/SearchRecipeBookCategory;)V",
            at = @At("TAIL")
        )
        private void setIcons(SearchRecipeBookCategory category, CallbackInfo info) {
            this.primaryIconItem = ItemIds.COMPASS;
            this.secondaryIconItem = Optional.empty();
        }

        @Inject(
            method = "<init>(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/crafting/RecipeBookCategory;)V",
            at = @At("TAIL")
        )
        private void setIcons(Item icon, RecipeBookCategory category, CallbackInfo info) {
            this.primaryIconItem = BuiltInRegistries.ITEM.getResourceKey(icon).orElseThrow();
            this.secondaryIconItem = Optional.empty();
        }

        @Inject(
            method = "<init>(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/crafting/RecipeBookCategory;)V",
            at = @At("TAIL")
        )
        private void setIcons(Item primaryIcon, Item secondaryIcon, RecipeBookCategory category, CallbackInfo info) {
            this.primaryIconItem = BuiltInRegistries.ITEM.getResourceKey(primaryIcon).orElseThrow();
            this.secondaryIconItem = Optional.of(BuiltInRegistries.ITEM.getResourceKey(secondaryIcon).orElseThrow());
        }

        @Override
        public ItemStack itematic$primaryIconItem(ItemAccess items) {
            return items.get(this.primaryIconItem)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }

        @Override
        public Optional<ItemStack> itematic$secondaryIconItem(ItemAccess items) {
            return this.secondaryIconItem.flatMap(items::get)
                .map(ItemStack::new);
        }
    }
}
