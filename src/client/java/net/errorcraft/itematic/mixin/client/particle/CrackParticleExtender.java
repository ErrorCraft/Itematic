package net.errorcraft.itematic.mixin.client.particle;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class CrackParticleExtender {
    @Mixin(CrackParticle.SnowballFactory.class)
    public static class SnowballFactoryExtender {
        @Redirect(
            method = "createParticle(Lnet/minecraft/particle/DefaultParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForSnowballUseCreateStack(ItemConvertible item, @Local(argsOnly = true) ClientWorld clientWorld) {
            return clientWorld.itematic$createStack(ItemKeys.SNOWBALL);
        }
    }

    @Mixin(CrackParticle.SlimeballFactory.class)
    public static class SlimeballFactoryExtender {
        @Redirect(
            method = "createParticle(Lnet/minecraft/particle/DefaultParticleType;Lnet/minecraft/client/world/ClientWorld;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForSlimeBallUseCreateStack(ItemConvertible item, @Local(argsOnly = true) ClientWorld clientWorld) {
            return clientWorld.itematic$createStack(ItemKeys.SLIME_BALL);
        }
    }
}
