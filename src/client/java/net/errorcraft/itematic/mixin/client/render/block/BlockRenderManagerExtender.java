package net.errorcraft.itematic.mixin.client.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerExtender {
    @Redirect(
        method = "renderBlockAsEntity",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, BlockState state) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return ItemStack.EMPTY;
        }
        return world.itematic$createStack(state.getBlock().itematic$asItemKey());
    }
}
