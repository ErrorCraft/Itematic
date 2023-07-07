package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

public class BrewingStandScreenHandlerExtender {
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
        private static boolean matchesIsOfUseItemComponentCheck(ItemStack instance, Item item) {
            return instance.hasComponent(ItemComponentTypes.POTION_HOLDER);
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
            return instance.isOf(ItemKeys.GLASS_BOTTLE);
        }
    }

    @Mixin(targets = "net/minecraft/screen/BrewingStandScreenHandler$FuelSlot")
    public static class FuelSlotExtender {
        @Redirect(
            method = "matches",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            )
        )
        private static boolean matchesIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.isOf(ItemKeys.BLAZE_POWDER);
        }
    }
}
