package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceMenu.class)
public class AbstractFurnaceScreenHandlerExtender {
    @Redirect(
        method = "isFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/FuelValues;isFuel(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean isFuelUseItemComponentCheck(FuelValues instance, ItemStack item) {
        return item.itematic$hasBehavior(ItemComponentTypes.FUEL);
    }
}
