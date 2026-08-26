package net.errorcraft.itematic.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerExtender {
    @Shadow
    @Final
    private ClientLevel level;

    @WrapOperation(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/item/Item;)Lnet/minecraft/core/particles/ItemParticleOption;",
            ordinal = 1
        )
    )
    private ItemParticleOption newItemStackTemplateForSplashPotionUseCreateStackTemplate(ParticleType<ItemParticleOption> type, Item item, Operation<ItemParticleOption> original) {
        return new ItemParticleOption(
            type,
            this.level.itematic$createStackTemplate(ItemIds.SPLASH_POTION)
        );
    }

    @WrapOperation(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/item/Item;)Lnet/minecraft/core/particles/ItemParticleOption;",
            ordinal = 0
        )
    )
    private ItemParticleOption newItemStackTemplateForEnderEyeUseCreateStackTemplate(ParticleType<ItemParticleOption> type, Item item, Operation<ItemParticleOption> original) {
        return new ItemParticleOption(
            type,
            this.level.itematic$createStackTemplate(ItemIds.ENDER_EYE)
        );
    }
}
