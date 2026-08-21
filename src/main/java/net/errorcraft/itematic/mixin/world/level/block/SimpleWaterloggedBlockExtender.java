package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SimpleWaterloggedBlock.class)
public interface SimpleWaterloggedBlockExtender {
    @Redirect(
        method = "pickupBlock",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWaterBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) LevelAccessor level) {
        return level.itematic$createStack(ItemIds.WATER_BUCKET);
    }
}
