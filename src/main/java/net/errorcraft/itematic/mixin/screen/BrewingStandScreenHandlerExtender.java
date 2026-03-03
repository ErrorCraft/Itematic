package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.access.screen.BrewingStandScreenHandlerAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BrewingStandScreenHandler.class)
public class BrewingStandScreenHandlerExtender implements BrewingStandScreenHandlerAccess {
    @Shadow
    @Final
    private PropertyDelegate propertyDelegate;

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/ArrayPropertyDelegate;<init>(I)V"
        )
    )
    private static int initAddMaxFuelTimeProperty(int size) {
        return size + 1;
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/BrewingStandScreenHandler;checkDataCount(Lnet/minecraft/screen/PropertyDelegate;I)V"
        )
    )
    private static int checkDataCountAddMaxFuelTimeProperty(int expectedCount) {
        return expectedCount + 1;
    }

    @Override
    public int itematic$maxBrewingTime() {
        return this.propertyDelegate.get(2);
    }

    @Mixin(targets = "net/minecraft/screen/BrewingStandScreenHandler$IngredientSlot")
    public static class IngredientSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;isValidIngredient(Lnet/minecraft/item/ItemStack;)Z"
            )
        )
        private static boolean isAlwaysValidIngredient(BrewingRecipeRegistry instance, ItemStack stack) {
            return true;
        }
    }

    @Mixin(targets = "net/minecraft/screen/BrewingStandScreenHandler$PotionSlot")
    public static class PotionSlotExtender {
        @Redirect(
            method = "matches",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            )
        )
        private static boolean matchesIsOfUseItemTagCheck(ItemStack instance, Item item) {
            return instance.isIn(ItematicItemTags.BREWING_INPUTS);
        }

        @Redirect(
            method = "matches",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;SPLASH_POTION:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                ),
                to = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean matchesIsOfRemainingItemChecksReturnFalse(ItemStack instance, Item item) {
            return false;
        }

        @Redirect(
            method = "matches",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean matchesIsOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.GLASS_BOTTLE);
        }
    }
}
