package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockExtender {
    @Redirect(
        method = "canEntityWalkOnPowderSnow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isLeatherBootsCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.LEATHER_BOOTS);
    }

    @Redirect(
        method = "pickupBlock",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPowderSnowBucketUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) LevelAccessor level) {
        return level.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET);
    }
}
