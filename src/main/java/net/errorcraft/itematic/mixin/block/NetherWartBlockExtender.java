package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetherWartBlock.class)
public class NetherWartBlockExtender {
    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, WorldView world) {
        return world.createStack(ItemKeys.NETHER_WART);
    }
}
