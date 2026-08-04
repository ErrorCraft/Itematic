package net.errorcraft.itematic.mixin.client.particle;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class CrackParticleExtender {
    @Mixin(BreakingItemParticle.SnowballProvider.class)
    public static class SnowballFactoryExtender {
        @Redirect(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForSnowballUseCreateStack(ItemLike item, @Local(argsOnly = true) ClientLevel clientWorld) {
            return clientWorld.itematic$createStack(ItemKeys.SNOWBALL);
        }
    }

    @Mixin(BreakingItemParticle.SlimeProvider.class)
    public static class SlimeballFactoryExtender {
        @Redirect(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForSlimeBallUseCreateStack(ItemLike item, @Local(argsOnly = true) ClientLevel clientWorld) {
            return clientWorld.itematic$createStack(ItemKeys.SLIME_BALL);
        }
    }
}
