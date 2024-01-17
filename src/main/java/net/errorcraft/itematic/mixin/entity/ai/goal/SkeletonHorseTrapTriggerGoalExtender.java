package net.errorcraft.itematic.mixin.entity.ai.goal;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public class SkeletonHorseTrapTriggerGoalExtender {
    @Shadow
    @Final
    private SkeletonHorseEntity skeletonHorse;

    @Redirect(
        method = "getSkeleton",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronHelmetUseCreateStack(ItemConvertible item) {
        return this.skeletonHorse.getWorld().itematic$createStack(ItemKeys.IRON_HELMET);
    }
}
