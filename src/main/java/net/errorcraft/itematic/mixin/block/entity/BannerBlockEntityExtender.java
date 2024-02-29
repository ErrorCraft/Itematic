package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BannerBlockEntityAccess;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BannerBlockEntity.class)
public abstract class BannerBlockEntityExtender implements BannerBlockEntityAccess {
    @Shadow
    public abstract ItemStack getPickStack();

    @Unique
    private ItemStack pickStack;

    @Redirect(
        method = "getPickStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item) {
        return this.pickStack;
    }

    @Override
    public ItemStack itematic$getPickStack(ItemStack stack) {
        this.pickStack = stack;
        stack = this.getPickStack();
        this.pickStack = null;
        return stack;
    }
}
