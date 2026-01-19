package net.errorcraft.itematic.item.shooter.method;

import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.item.shooter.method.methods.DirectShooterMethod;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ShooterMethodTypes {
    public static final ShooterMethodType<DirectShooterMethod> DIRECT = register(ShooterMethodTypeKeys.DIRECT, new ShooterMethodType<>(DirectShooterMethod.CODEC));
    public static final ShooterMethodType<ChargeableShooterMethod> CHARGEABLE = register(ShooterMethodTypeKeys.CHARGEABLE, new ShooterMethodType<>(ChargeableShooterMethod.CODEC));

    private ShooterMethodTypes() {}

    public static void init() {}

    private static <T extends ShooterMethod> ShooterMethodType<T> register(RegistryKey<ShooterMethodType<?>> id, ShooterMethodType<T> type) {
        return Registry.register(ItematicRegistries.SHOOTER_METHOD_TYPE, id, type);
    }
}
