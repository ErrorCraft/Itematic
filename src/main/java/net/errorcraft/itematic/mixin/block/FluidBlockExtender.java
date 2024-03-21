package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidBlock.class)
public class FluidBlockExtender {
    @Shadow
    @Final
    protected FlowableFluid fluid;

    @Redirect(
        method = "tryDrainFluid",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, @Local(argsOnly = true) WorldAccess world) {
        return world.itematic$createStack(this.fluid.getBucketItemKey());
    }
}
