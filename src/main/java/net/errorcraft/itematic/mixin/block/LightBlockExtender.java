package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightBlock.class)
public class LightBlockExtender {
    @ModifyArg(
        method = "getShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private Item getLightUseDynamicRegistry(Item item, @Local(argsOnly = true) BlockGetter world) {
        if (world instanceof ItemAccess itemAccess) {
            return itemAccess.getOptionalEntry(ItemKeys.LIGHT)
                .map(Holder::value)
                .orElse(null);
        }
        return null;
    }
}
