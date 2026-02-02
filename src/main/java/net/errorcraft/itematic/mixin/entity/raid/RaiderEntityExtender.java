package net.errorcraft.itematic.mixin.entity.raid;

import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RaiderEntity.class)
public class RaiderEntityExtender extends PatrolEntity {
    protected RaiderEntityExtender(EntityType<? extends PatrolEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
        method = "isCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;createOminousBanner(Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(CallbackInfoReturnable<Boolean> info) {
        RaidUtil.createOminousBanner(this.getWorld());
    }

    @Inject(
        method = "loot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;createOminousBanner(Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(CallbackInfo info) {
        RaidUtil.createOminousBanner(this.getWorld());
    }

    @Inject(
        method = "method_16483",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;createOminousBanner(Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static void createOminousBannerSetDataDrivenItemStack(ItemEntity itemEntity, CallbackInfoReturnable<Boolean> info) {
        RaidUtil.createOminousBanner(itemEntity.getWorld());
    }

    @Mixin(RaiderEntity.PickUpBannerAsLeaderGoal.class)
    public static class PickUpBannerAsLeaderGoalExtender<T extends RaiderEntity> {
        @Shadow
        @Final
        private T actor;

        @Inject(
            method = "shouldStop",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/village/raid/Raid;createOminousBanner(Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private void createOminousBannerSetDataDrivenItemStack(CallbackInfoReturnable<Boolean> info) {
            RaidUtil.createOminousBanner(this.actor.getWorld());
        }
    }
}
