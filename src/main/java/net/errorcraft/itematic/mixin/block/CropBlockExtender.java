package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.CropBlockAccess;
import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CropBlock.class)
public class CropBlockExtender implements CropBlockAccess {
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
        return new ItemStack(access.getItem(this.getSeedsItemKey()));
    }
}
