package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bucketable.class)
public interface BucketableExtender {
    @Redirect(
        method = "tryBucket",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;WATER_BUCKET:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Item tryBucketGetWaterBucketUseDynamicRegistry(PlayerEntity player) {
        return player.getWorld().itematic$getItem(ItemKeys.WATER_BUCKET).value();
    }
}
