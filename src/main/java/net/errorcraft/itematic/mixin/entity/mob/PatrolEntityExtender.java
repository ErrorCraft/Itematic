package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.village.raid.RaidUtil;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PatrolEntity.class)
public class PatrolEntityExtender {
    @Redirect(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/village/raid/Raid;getOminousBanner()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getOminousBannerUseRegistryEntry(ServerWorldAccess world) {
        return RaidUtil.createOminousBanner(world);
    }
}
