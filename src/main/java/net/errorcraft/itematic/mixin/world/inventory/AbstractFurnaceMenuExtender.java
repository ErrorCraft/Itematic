package net.errorcraft.itematic.mixin.world.inventory;

import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceMenu.class)
public class AbstractFurnaceMenuExtender {
    @Redirect(
        method = "isFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/FuelValues;isFuel(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean isFuelCheckItemBehavior(FuelValues instance, ItemStack item) {
        return item.itematic$hasBehavior(ItemBehaviorType.FUEL);
    }
}
