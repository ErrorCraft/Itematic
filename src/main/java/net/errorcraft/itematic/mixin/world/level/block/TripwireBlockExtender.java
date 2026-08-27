package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TripWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TripWireBlock.class)
public class TripwireBlockExtender {
    @Redirect(
        method = "playerWillDestroy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isShearsCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.SHEARS);
    }
}
