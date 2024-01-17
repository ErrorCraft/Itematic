package net.errorcraft.itematic.mixin.entity.raid;

import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RaiderEntity.class)
public class RaiderEntityExtender extends PatrolEntity {
    protected RaiderEntityExtender(EntityType<? extends PatrolEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = { "onDeath", "loot" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerUseRegistryEntry() {
        return RaidUtil.createOminousBanner(this.getWorld());
    }

    @Mixin(RaiderEntity.PickupBannerAsLeaderGoal.class)
    public static class PickupBannerAsLeaderGoalExtender<T extends RaiderEntity> {
        @Shadow
        @Final
        private T actor;

        @Redirect(
            method = "canStart",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack getOminousBannerUseRegistryEntry() {
            return RaidUtil.createOminousBanner(this.actor.getWorld());
        }
    }
}
