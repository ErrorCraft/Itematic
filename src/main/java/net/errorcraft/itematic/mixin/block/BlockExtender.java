package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public abstract class BlockExtender {
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
        RegistryKey<Item> key = ItemUtil.keyFromBlock((Block)(Object) this);
        return access.getItemAccess().getOptionalEntry(key)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
