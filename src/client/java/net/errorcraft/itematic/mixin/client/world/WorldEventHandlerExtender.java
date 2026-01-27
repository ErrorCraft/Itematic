package net.errorcraft.itematic.mixin.client.world;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.world.WorldEventHandler;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WorldEventHandler.class)
public class WorldEventHandlerExtender {
    @Shadow
    @Final
    private World world;

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
