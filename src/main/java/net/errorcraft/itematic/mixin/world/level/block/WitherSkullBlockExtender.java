package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WitherSkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockExtender {
    @Redirect(
        method = "canSpawnMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isWitherSkeletonSkullCheckId(ItemStack instance, Object o) {
        return instance.is(BlockItemIds.WITHER_SKELETON_SKULL.item());
    }
}
