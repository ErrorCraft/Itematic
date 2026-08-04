package net.errorcraft.itematic.mixin.entity.raid;

import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Raider.class)
public class RaiderEntityExtender extends PatrollingMonster {
    protected RaiderEntityExtender(EntityType<? extends PatrollingMonster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
        method = "isCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(CallbackInfoReturnable<Boolean> info) {
        RaidUtil.createOminousBanner(this.level());
    }

    @Inject(
        method = "pickUpItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void createOminousBannerSetDataDrivenItemStack(CallbackInfo info) {
        RaidUtil.createOminousBanner(this.level());
    }

    @Inject(
        method = "method_16483",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static void createOminousBannerSetDataDrivenItemStack(ItemEntity itemEntity, CallbackInfoReturnable<Boolean> info) {
        RaidUtil.createOminousBanner(itemEntity.level());
    }

    @Mixin(Raider.ObtainRaidLeaderBannerGoal.class)
    public static class PickUpBannerAsLeaderGoalExtender<T extends Raider> {
        @Shadow
        @Final
        private T mob;

        @Inject(
            method = "cannotPickUpBanner",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private void createOminousBannerSetDataDrivenItemStack(CallbackInfoReturnable<Boolean> info) {
            RaidUtil.createOminousBanner(this.mob.level());
        }
    }
}
