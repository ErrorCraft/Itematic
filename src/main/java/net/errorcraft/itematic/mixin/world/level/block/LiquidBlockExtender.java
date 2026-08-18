package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LiquidBlock.class)
public class LiquidBlockExtender {
    @Shadow
    @Final
    protected FlowingFluid fluid;

    @Redirect(
        method = "pickupBlock",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, @Local(argsOnly = true) LevelAccessor level) {
        ResourceKey<Item> itemId = this.fluid.itematic$getBucketItemId();
        if (itemId == null) {
            return ItemStack.EMPTY;
        }

        return level.itematic$createStack(itemId);
    }
}
