package net.errorcraft.itematic.mixin.world.entity.raid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raider.class)
public class RaiderExtender extends PatrollingMonster {
    protected RaiderExtender(EntityType<? extends PatrollingMonster> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = {
            "isCaptain",
            "pickUpItem"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerInstanceUseHolder(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original) {
        return ItematicRaids.ominousBanner(
            this.level().itematic$createStack(ItemIds.WHITE_BANNER),
            patternGetter
        );
    }

    @WrapOperation(
        method = "lambda$static$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack getOminousBannerInstanceUseHolderStatic(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original, ItemEntity e) {
        return ItematicRaids.ominousBanner(
            e.level().itematic$createStack(ItemIds.WHITE_BANNER),
            patternGetter
        );
    }

    @Mixin(Raider.ObtainRaidLeaderBannerGoal.class)
    public static class ObtainRaidLeaderBannerGoalExtender<T extends Raider> {
        @Shadow
        @Final
        private T mob;

        @WrapOperation(
            method = "cannotPickUpBanner",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack getOminousBannerInstanceUseHolder(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original) {
            return ItematicRaids.ominousBanner(
                this.mob.level().itematic$createStack(ItemIds.WHITE_BANNER),
                patternGetter
            );
        }
    }
}
