package net.errorcraft.itematic.mixin.client.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class BreakingItemParticleExtender {
    @Mixin(BreakingItemParticle.SlimeProvider.class)
    public static class SlimeProviderExtender {
        @WrapOperation(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStackTemplate;"
            )
        )
        private ItemStackTemplate newItemStackTemplateForSlimeBallUseCreateStackTemplate(Item item, Operation<ItemStackTemplate> original, @Local(name = "level", argsOnly = true) ClientLevel level) {
            return level.itematic$createStackTemplate(ItemIds.SLIME_BALL);
        }
    }

    @Mixin(BreakingItemParticle.CobwebProvider.class)
    public static class CobwebProviderExtender {
        @WrapOperation(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStackTemplate;"
            )
        )
        private ItemStackTemplate newItemStackTemplateForCobwebUseCreateStackTemplate(Item item, Operation<ItemStackTemplate> original, @Local(name = "level", argsOnly = true) ClientLevel level) {
            return level.itematic$createStackTemplate(ItemIds.COBWEB);
        }
    }

    @Mixin(BreakingItemParticle.SnowballProvider.class)
    public static class SnowballProviderExtender {
        @WrapOperation(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStackTemplate;"
            )
        )
        private ItemStackTemplate newItemStackTemplateForSnowballUseCreateStackTemplate(Item item, Operation<ItemStackTemplate> original, @Local(name = "level", argsOnly = true) ClientLevel level) {
            return level.itematic$createStackTemplate(ItemIds.SNOWBALL);
        }
    }
}
