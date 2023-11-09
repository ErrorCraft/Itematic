package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BubbleColumnBlock.class)
public class BubbleColumnBlockExtender {
    @Redirect(
        method = "tryDrainFluid",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack tryDrainFluidNewItemStackUseRegistryEntry(ItemConvertible item, @Local WorldAccess world) {
        return new ItemStack(world.itematic$getItem(ItemKeys.WATER_BUCKET));
    }
}
