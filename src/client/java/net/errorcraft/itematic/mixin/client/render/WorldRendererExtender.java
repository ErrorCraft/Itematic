package net.errorcraft.itematic.mixin.client.render;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WorldRenderer.class)
public class WorldRendererExtender {
    @Shadow
    private ClientWorld world;

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "net/minecraft/particle/ItemStackParticleEffect",
                ordinal = 1
            )
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.SPLASH_POTION);
    }

    @Redirect(
        method = "processWorldEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/particle/ParticleTypes;ITEM:Lnet/minecraft/particle/ParticleType;",
                opcode = Opcodes.GETSTATIC,
                ordinal = 0
            )
        )
    )
    private ItemStack newItemStackForEnderEyeUseCreateStack(ItemConvertible item) {
        return this.world.itematic$createStack(ItemKeys.ENDER_EYE);
    }
}
