package net.errorcraft.itematic.mixin.world.entity.raid;

import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
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
public class RaiderExtender extends PatrollingMonster {
    protected RaiderExtender(EntityType<? extends PatrollingMonster> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "isCaptain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void setOminousBannerForLaterUse(CallbackInfoReturnable<Boolean> info) {
        ItematicRaids.createOminousBanner(this.level());
    }

    @Inject(
        method = "pickUpItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private void setOminousBannerForLaterUse(CallbackInfo info) {
        ItematicRaids.createOminousBanner(this.level());
    }

    @Inject(
        method = "lambda$static$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static void setOminousBannerForLaterUse(ItemEntity e, CallbackInfoReturnable<Boolean> info) {
        ItematicRaids.createOminousBanner(e.level());
    }

    @Mixin(Raider.ObtainRaidLeaderBannerGoal.class)
    public static class ObtainRaidLeaderBannerGoalExtender<T extends Raider> {
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
        private void setOminousBannerForLaterUse(CallbackInfoReturnable<Boolean> info) {
            ItematicRaids.createOminousBanner(this.mob.level());
        }
    }
}
