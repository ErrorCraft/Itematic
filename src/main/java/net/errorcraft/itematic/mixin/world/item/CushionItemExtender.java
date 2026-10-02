package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.world.item.behavior.behaviors.EntityItemBehavior;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CushionItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CushionItem.class)
public class CushionItemExtender {
    @WrapOperation(
        method = "recalculateContextForSpecialCollisionShapes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean isCushionUsesCollisionShapeUseItemBehavior(BlockState instance, TagKey<Block> tagKey, Operation<Boolean> original) {
        return instance.is(EntityItemBehavior.USES_COLLISION_SHAPE.get());
    }
}
