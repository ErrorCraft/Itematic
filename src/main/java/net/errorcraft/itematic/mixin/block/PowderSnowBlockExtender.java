package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.Item;
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
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfForLeatherBootsUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.LEATHER_BOOTS);
    }

    @Redirect(
        method = "pickupBlock",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForPowderSnowBucketUseCreateStack(ItemLike item, @Local(argsOnly = true) LevelAccessor world) {
        return world.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET);
    }
}
