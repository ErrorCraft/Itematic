package net.errorcraft.itematic.mixin.world.entity.projectile;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingHook.class)
public class FishingHookExtender {
    @Redirect(
        method = "shouldStopFishing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isFishingRodCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.FISHING_ROD);
    }
}
