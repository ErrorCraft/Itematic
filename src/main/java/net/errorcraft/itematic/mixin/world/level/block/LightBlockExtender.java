package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightBlock.class)
public class LightBlockExtender {
    @WrapOperation(
        method = "getShape",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingLightCheckId(CollisionContext instance, Item item, Operation<Boolean> original) {
        return instance.itematic$isHoldingItem(ItemIds.LIGHT);
    }
}
