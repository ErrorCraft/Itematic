package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockExtender {
    @Redirect(
        method = "isRespawnFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isGlowstoneCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GLOWSTONE);
    }
}
