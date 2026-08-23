package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.TransmuteResult;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TransmuteResult.class)
public class TransmuteResultExtender {
    @Shadow
    @Final
    private Holder<Item> item;

    @Redirect(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyUseHolder(ItemStack instance, ItemLike newItem, int newCount) {
        return instance.itematic$transmuteCopy(this.item, newCount);
    }
}
