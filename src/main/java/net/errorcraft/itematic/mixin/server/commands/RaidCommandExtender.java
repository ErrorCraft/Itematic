package net.errorcraft.itematic.mixin.server.commands;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.raid.ItematicRaids;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderGetter;
import net.minecraft.server.commands.RaidCommand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RaidCommand.class)
public class RaidCommandExtender {
    @WrapOperation(
        method = "spawnLeader",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/raid/Raid;getOminousBannerInstance(Lnet/minecraft/core/HolderGetter;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack getOminousBannerInstanceUseHolder(HolderGetter<BannerPattern> patternGetter, Operation<ItemStack> original, CommandSourceStack source) {
        return ItematicRaids.ominousBanner(
            source.getLevel().itematic$createStack(ItemIds.WHITE_BANNER),
            patternGetter
        );
    }
}
