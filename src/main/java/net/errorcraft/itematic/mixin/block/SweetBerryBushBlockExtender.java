package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockExtender {
    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getPickStackNewItemStackUseRegistryEntry(ItemConvertible item, BlockView world) {
        if (!(world instanceof WorldViewAccess access)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(access.getItem(ItemKeys.SWEET_BERRIES));
    }

    @Redirect(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean onUseIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.BONE_MEAL);
    }

    @Redirect(
        method = "onUse",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack onUseNewItemStackUseRegistryEntry(ItemConvertible item, int count, @Local World world) {
        return new ItemStack(world.getItem(ItemKeys.SWEET_BERRIES));
    }
}
