package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
    @Accessor("DEFAULT_PULL_TIME")
    static int defaultChargeTime() {
        throw new AssertionError();
    }

    @Accessor("CHARGE_PROGRESS")
    static float startSoundProgress() {
        throw new AssertionError();
    }

    @Accessor("LOAD_PROGRESS")
    static float midSoundProgress() {
        throw new AssertionError();
    }

    @Accessor("DEFAULT_SPEED")
    static float defaultPower() {
        throw new AssertionError();
    }

    @Accessor("FIREWORK_ROCKET_SPEED")
    static float fireworkRocketPower() {
        throw new AssertionError();
    }
}
