package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Consumable.class)
public class ConsumableExtender {
    @ModifyExpressionValue(
        method = "emitParticlesAndSounds",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/component/Consumable;hasConsumeParticles:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean useDataComponent(boolean original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return itemStack.has(ItematicDataComponents.HAS_USE_PARTICLES);
    }

    @ModifyExpressionValue(
        method = "emitParticlesAndSounds",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/component/Consumable;animation:Lnet/minecraft/world/item/ItemUseAnimation;",
            opcode = Opcodes.GETFIELD
        )
    )
    private ItemUseAnimation useDataComponent(ItemUseAnimation original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return itemStack.getUseAnimation();
    }

    @ModifyExpressionValue(
        method = "emitParticlesAndSounds",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;"
        )
    )
    @Nullable
    @SuppressWarnings("unchecked")
    private <T> T useDataComponent(T original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        Holder<SoundEvent> useSound = itemStack.get(ItematicDataComponents.USE_SOUND);
        if (useSound != null) {
            return (T) useSound.value();
        }

        return null;
    }

    @WrapWithCondition(
        method = "emitParticlesAndSounds",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
        )
    )
    private boolean checkNull(LivingEntity instance, @Nullable SoundEvent soundEvent, float volume, float pitch) {
        return soundEvent != null;
    }
}
