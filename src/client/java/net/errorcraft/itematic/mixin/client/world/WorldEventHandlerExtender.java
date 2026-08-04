package net.errorcraft.itematic.mixin.client.world;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LevelEventHandler.class)
public class WorldEventHandlerExtender {
    @Shadow
    @Final
    private ClientLevel level;

    @Redirect(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "net/minecraft/core/particles/ItemParticleOption",
                ordinal = 1
            )
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(ItemLike item) {
        return this.level.itematic$createStack(ItemKeys.SPLASH_POTION);
    }

    @Redirect(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/particles/ParticleTypes;ITEM:Lnet/minecraft/core/particles/ParticleType;",
                opcode = Opcodes.GETSTATIC,
                ordinal = 0
            )
        )
    )
    private ItemStack newItemStackForEnderEyeUseCreateStack(ItemLike item) {
        return this.level.itematic$createStack(ItemKeys.ENDER_EYE);
    }
}
