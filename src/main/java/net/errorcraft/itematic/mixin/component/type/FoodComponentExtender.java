package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class FoodComponentExtender {
    @Mixin(FoodComponent.Builder.class)
    public static class BuilderExtender {
        @Redirect(
            method = "usingConvertsTo",
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
