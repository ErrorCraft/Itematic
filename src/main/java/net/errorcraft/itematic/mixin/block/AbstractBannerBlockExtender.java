package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.entity.BannerBlockEntityAccess;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBannerBlock.class)
public abstract class AbstractBannerBlockExtender extends BlockWithEntity {
    protected AbstractBannerBlockExtender(Settings settings) {
        super(settings);
    }

    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BannerBlockEntity;getPickStack()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getPickStackUseCreateStack(BannerBlockEntity instance, WorldView world, BlockPos pos, BlockState state) {
        return ((BannerBlockEntityAccess) instance).itematic$getPickStack(super.getPickStack(world, pos, state));
    }
}
