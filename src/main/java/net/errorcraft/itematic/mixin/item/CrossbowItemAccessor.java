package net.errorcraft.itematic.mixin.item;

import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
    @Accessor("MAX_CHARGE_DURATION")
    static float defaultChargeTime() {
        throw new AssertionError();
    }

    @Accessor("START_SOUND_PERCENT")
    static float startSoundProgress() {
        throw new AssertionError();
    }

    @Accessor("MID_SOUND_PERCENT")
    static float midSoundProgress() {
        throw new AssertionError();
    }

    @Accessor("ARROW_POWER")
    static float defaultPower() {
        throw new AssertionError();
    }

    @Accessor("FIREWORK_POWER")
    static float fireworkRocketPower() {
        throw new AssertionError();
    }

    @Accessor("DEFAULT_SOUNDS")
    static CrossbowItem.ChargingSounds defaultChargingSounds() {
        throw new AssertionError();
    }
}
