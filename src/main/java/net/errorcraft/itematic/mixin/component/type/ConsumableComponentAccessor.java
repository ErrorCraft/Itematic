package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.component.type.ConsumableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConsumableComponent.class)
public interface ConsumableComponentAccessor {
    @Accessor("PARTICLES_AND_SOUND_TICK_THRESHOLD")
    static float consumeEffectsThreshold() {
        throw new AssertionError();
    }
}
