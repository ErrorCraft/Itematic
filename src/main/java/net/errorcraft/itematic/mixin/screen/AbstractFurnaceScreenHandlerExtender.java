package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceScreenHandler.class)
public class AbstractFurnaceScreenHandlerExtender {
    @Redirect(
        method = "isFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/FuelRegistry;isFuel(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isFuelUseItemComponentCheck(FuelRegistry instance, ItemStack item) {
        return item.itematic$hasBehavior(ItemComponentTypes.FUEL);
    }
}
