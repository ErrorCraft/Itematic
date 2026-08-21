package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.NetherWartBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetherWartBlock.class)
public class NetherWartBlockExtender {
    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForNetherWartUseCreateStack(ItemLike item, LevelReader level) {
        return level.itematic$createStack(ItemIds.NETHER_WART);
    }
}
