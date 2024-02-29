package net.errorcraft.itematic.mixin.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(CrossbowItem.class)
public interface CrossbowItemAccessor {
    @Accessor("DEFAULT_PULL_TIME")
    static int defaultPullTime() {
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

    @Invoker("loadProjectiles")
    static boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        throw new AssertionError();
    }

    @Invoker("getProjectiles")
    static List<ItemStack> getProjectiles(ItemStack crossbow) {
        throw new AssertionError();
    }
}
