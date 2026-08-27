package net.errorcraft.itematic.mixin.world.entity.monster;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PatrollingMonster.class)
public class PatrollingMonsterExtender {
    @WrapOperation(
        method = "finalizeSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerInstanceUseHolder(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original, ServerLevelAccessor level) {
        return ItematicRaids.ominousBanner(ItemIds.WHITE_BANNER, level);
    }
}
