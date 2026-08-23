package net.errorcraft.itematic.mixin.world.inventory;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FurnaceFuelSlot.class)
public class FurnaceFuelSlotExtender {
    @Redirect(
        method = "isBucket",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBucketCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.BUCKET);
    }
}
