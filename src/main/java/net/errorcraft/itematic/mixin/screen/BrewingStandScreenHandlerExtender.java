package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.access.screen.BrewingStandScreenHandlerAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BrewingStandMenu.class)
public class BrewingStandScreenHandlerExtender implements BrewingStandScreenHandlerAccess {
    @Shadow
    @Final
    private ContainerData brewingStandData;

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/SimpleContainerData;<init>(I)V"
        )
    )
    private static int initAddMaxFuelTimeProperty(int size) {
        return size + 1;
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/BrewingStandMenu;checkContainerDataCount(Lnet/minecraft/world/inventory/ContainerData;I)V"
        )
    )
    private static int checkDataCountAddMaxFuelTimeProperty(int expectedCount) {
        return expectedCount + 1;
    }

    @Override
    public int itematic$maxBrewingTime() {
        return this.brewingStandData.get(2);
    }

    @Mixin(targets = "net/minecraft/world/inventory/BrewingStandMenu$IngredientsSlot")
    public static class IngredientSlotExtender {
        @Redirect(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/alchemy/PotionBrewing;isIngredient(Lnet/minecraft/world/item/ItemStack;)Z"
            )
        )
        private static boolean isAlwaysValidIngredient(PotionBrewing instance, ItemStack stack) {
            return true;
        }
    }

    @Mixin(targets = "net/minecraft/world/inventory/BrewingStandMenu$PotionSlot")
    public static class PotionSlotExtender {
        @Redirect(
            method = "mayPlaceItem",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                ordinal = 0
            )
        )
        private static boolean matchesIsOfUseItemTagCheck(ItemStack instance, Item item) {
            return instance.is(ItematicItemTags.BREWING_INPUTS);
        }

        @Redirect(
            method = "mayPlaceItem",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;SPLASH_POTION:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETSTATIC
                ),
                to = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;GLASS_BOTTLE:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean matchesIsOfRemainingItemChecksReturnFalse(ItemStack instance, Item item) {
            return false;
        }

        @Redirect(
            method = "mayPlaceItem",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;GLASS_BOTTLE:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean matchesIsOfForGlassBottleUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.GLASS_BOTTLE);
        }
    }
}
