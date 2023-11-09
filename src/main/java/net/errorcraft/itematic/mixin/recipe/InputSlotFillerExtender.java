package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InputSlotFiller.class)
public class InputSlotFillerExtender<C extends Inventory> {
    private ItemAccess itemAccess;

    @Inject(
        method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V",
        at = @At("HEAD")
    )
    private void fillInputSlotsSetItemAccess(ServerPlayerEntity entity, @Nullable RecipeEntry<? extends Recipe<C>> recipe, boolean craftAll, CallbackInfo info) {
        if (this.itemAccess == null) {
            this.itemAccess = entity.getWorld().itematic$getItemAccess();
        }
    }

    @Redirect(
        method = { "fillInputSlots(Lnet/minecraft/recipe/RecipeEntry;Z)V", "acceptAlignedInput" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/RecipeMatcher;getStackFromId(I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack fillInputSlotsGetStackFromIdUseItemAccess(int itemId) {
        if (itemId == 0) {
            return ItemStack.EMPTY;
        }
        Optional<? extends RegistryEntry<Item>> entry = this.itemAccess.getOptionalEntry(itemId);
        return entry.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
}
