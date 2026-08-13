package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.SkeletonTrapGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonTrapGoal.class)
public class SkeletonHorseTrapTriggerGoalExtender {
    @Shadow
    @Final
    private SkeletonHorse horse;

    @Redirect(
        method = "createSkeleton",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronHelmetUseCreateStack(ItemLike item) {
        return this.horse.level().itematic$createStack(ItemIds.IRON_HELMET);
    }
}
