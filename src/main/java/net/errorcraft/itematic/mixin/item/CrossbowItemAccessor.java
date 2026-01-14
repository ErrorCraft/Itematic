package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
    @Accessor("DEFAULT_PULL_TIME")
    static float defaultChargeTime() {
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
    static float defaultSpeed() {
        throw new AssertionError();
    }

    @Accessor("FIREWORK_ROCKET_SPEED")
    static float fireworkRocketSpeed() {
        throw new AssertionError();
    }

    @Accessor("DEFAULT_LOADING_SOUNDS")
    static CrossbowItem.LoadingSounds defaultChargingSounds() {
        throw new AssertionError();
    }

    @Invoker("loadProjectiles")
    @Contract
    static boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        throw new AssertionError();
    }
}
