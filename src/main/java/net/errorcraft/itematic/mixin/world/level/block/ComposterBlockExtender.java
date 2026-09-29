package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockExtender {
    @Redirect(
        method = "extractProduce",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) Level level) {
        return level.itematic$createStack(ItemIds.BONE_MEAL);
    }

    @Redirect(
        method = "getContainer",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBoneMealUseCreateStack(ItemLike item, @Local(name = "level", argsOnly = true) LevelAccessor level) {
        return level.itematic$createStack(ItemIds.BONE_MEAL);
    }

    @Mixin(targets = "net/minecraft/world/level/block/ComposterBlock$OutputContainer")
    public static class OutputContainerExtender {
        @Redirect(
            method = "canTakeItemThroughFace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isBoneMealCheckId(ItemStack instance, Object o) {
            return instance.is(ItemIds.BONE_MEAL);
        }
    }
}
