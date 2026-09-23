package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Bucketable.class)
public interface BucketableExtender {
    @WrapMethod(
        method = "canBePickedUpWithBucket"
    )
    private boolean isWaterBucketCheckId(ItemStack itemStack, Operation<Boolean> original) {
        return itemStack.is(ItemIds.WATER_BUCKET);
    }
}
