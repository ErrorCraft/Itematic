package net.errorcraft.itematic.world.item.weapon.shooter.method;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.DirectShooterMethod;
import net.minecraft.core.Registry;

public record ShooterMethodType<T extends ShooterMethod>(MapCodec<T> codec) {
    public static final ShooterMethodType<DirectShooterMethod> DIRECT = register(
        "direct",
        new ShooterMethodType<>(DirectShooterMethod.CODEC)
    );
    public static final ShooterMethodType<ChargeableShooterMethod> CHARGEABLE = register(
        "chargeable",
        new ShooterMethodType<>(ChargeableShooterMethod.CODEC)
    );

    public static void init() {}

    private static <T extends ShooterMethod> ShooterMethodType<T> register(String id, ShooterMethodType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.SHOOTER_METHOD_TYPE, id, type);
    }
}
