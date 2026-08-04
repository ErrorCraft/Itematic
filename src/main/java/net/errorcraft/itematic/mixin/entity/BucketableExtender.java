package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bucketable.class)
public interface BucketableExtender {
    @Redirect(
        method = "bucketMobPickup",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;WATER_BUCKET:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Item tryBucketGetWaterBucketUseDynamicRegistry(Player player) {
        return player.level().itematic$getItem(ItemKeys.WATER_BUCKET).value();
    }
}
