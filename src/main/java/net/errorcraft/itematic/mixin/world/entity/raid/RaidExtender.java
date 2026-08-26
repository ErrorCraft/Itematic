package net.errorcraft.itematic.mixin.world.entity.raid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raid.class)
public abstract class RaidExtender {
    @WrapOperation(
        method = "setLeader",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerInstanceUseHolder(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original, @Local(name = "raider", argsOnly = true) Raider raider) {
        return ItematicRaids.ominousBanner(
            raider.level().itematic$createStack(ItemIds.WHITE_BANNER),
            patternGetter
        );
    }
}
