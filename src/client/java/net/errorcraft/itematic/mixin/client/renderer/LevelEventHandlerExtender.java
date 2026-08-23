package net.errorcraft.itematic.mixin.client.renderer;

import net.errorcraft.itematic.references.ItemIds;
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
public class LevelEventHandlerExtender {
    @Shadow
    @Final
    private ClientLevel level;

    @Redirect(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        ),
        slice = @Slice(
            from = @At(
                value = "NEW",
                target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/core/particles/ItemParticleOption;",
                ordinal = 1
            )
        )
    )
    private ItemStack newItemStackForSplashPotionUseCreateStack(ItemLike item) {
        return this.level.itematic$createStack(ItemIds.SPLASH_POTION);
    }

    @Redirect(
        method = "levelEvent",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
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
        return this.level.itematic$createStack(ItemIds.ENDER_EYE);
    }
}
