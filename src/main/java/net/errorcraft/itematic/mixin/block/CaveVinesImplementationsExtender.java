package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.CaveVinesBodyBlock;
import net.minecraft.block.CaveVinesHeadBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ CaveVinesBodyBlock.class, CaveVinesHeadBlock.class })
public class CaveVinesImplementationsExtender {
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
        return new ItemStack(access.getItem(ItemKeys.GLOW_BERRIES));
    }
}
