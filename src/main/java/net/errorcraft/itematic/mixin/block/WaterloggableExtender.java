package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Waterloggable;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Waterloggable.class)
public interface WaterloggableExtender {
    @Redirect(
        method = "tryDrainFluid",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForWaterBucketUseCreateStack(ItemConvertible item, @Local(argsOnly = true) WorldAccess world) {
        return world.itematic$createStack(ItemKeys.WATER_BUCKET);
    }
}
