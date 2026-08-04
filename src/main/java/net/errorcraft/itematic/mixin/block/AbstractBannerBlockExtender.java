package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.entity.BannerBlockEntityAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBannerBlock.class)
public abstract class AbstractBannerBlockExtender extends BaseEntityBlock {
    protected AbstractBannerBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "getCloneItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/BannerBlockEntity;getItem()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getPickStackUseCreateStack(BannerBlockEntity instance, LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return ((BannerBlockEntityAccess) instance).itematic$getPickStack(super.getCloneItemStack(world, pos, state, includeData));
    }
}
