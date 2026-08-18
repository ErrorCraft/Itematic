package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import org.jspecify.annotations.Nullable;
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
    @Nullable
    private Item getLightUseDynamicRegistry(Item item, @Local(argsOnly = true) BlockGetter level) {
        if (level instanceof ItemAccess itemAccess) {
            return itemAccess.get(ItemIds.LIGHT)
                .map(Holder::value)
                .orElse(null);
        }

        return null;
    }
}
