package net.errorcraft.itematic.world.item.weapon.shooter.method;

import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Direct;
import net.minecraft.core.Registry;

public class ShooterMethods {
    private ShooterMethods() {}

    public static void bootstrap(Registry<ShooterMethod<?>> registry) {
        Registry.register(registry, "direct", Direct.INSTANCE);
        Registry.register(registry, "chargeable", Chargeable.INSTANCE);
    }
}
