package net.errorcraft.itematic.mixin.world.item.component;

import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Consumable.class)
public interface ConsumableAccessor {
    @Accessor("CONSUME_EFFECTS_START_FRACTION")
    static float consumeEffectsThreshold() {
        throw new AssertionError();
    }
}
