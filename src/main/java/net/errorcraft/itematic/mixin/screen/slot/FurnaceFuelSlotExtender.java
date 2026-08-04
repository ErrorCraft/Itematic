package net.errorcraft.itematic.mixin.screen.slot;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.Item;
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
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfForBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUCKET);
    }
}
