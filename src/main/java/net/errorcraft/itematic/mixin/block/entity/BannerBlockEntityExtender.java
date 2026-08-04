package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BannerBlockEntityAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityExtender implements BannerBlockEntityAccess {
    @Shadow
    public abstract ItemStack getItem();

    @Unique
    private ItemStack pickStack;

    @Redirect(
        method = "getItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item) {
        return this.pickStack;
    }

    @Override
    public ItemStack itematic$getPickStack(ItemStack stack) {
        this.pickStack = stack;
        stack = this.getItem();
        this.pickStack = null;
        return stack;
    }
}
