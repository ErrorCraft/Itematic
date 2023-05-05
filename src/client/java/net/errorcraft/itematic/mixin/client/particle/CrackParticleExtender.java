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
                target = "net/minecraft/item/ItemStack"
            )
        )
        private ItemStack createParticleNewItemStackUseRegistryEntry(ItemConvertible item, @Local ClientWorld clientWorld) {
            return new ItemStack(clientWorld.getItem(ItemKeys.SNOWBALL));
        }
    }
}
