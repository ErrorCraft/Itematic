package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.SetItemFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SetItemFunction.class)
public class SetItemFunctionExtender {
    @Shadow
    @Final
    private Holder<Item> item;

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyUseHolder(ItemStack instance, ItemLike newItem) {
        return instance.itematic$transmuteCopy(this.item);
    }
}
