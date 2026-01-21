package net.errorcraft.itematic.item.shooter.method;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ShooterMethodTypeKeys {
    public static final RegistryKey<ShooterMethodType<?>> DIRECT = of("direct");
    public static final RegistryKey<ShooterMethodType<?>> CHARGEABLE = of("chargeable");

    private ShooterMethodTypeKeys() {}

    private static RegistryKey<ShooterMethodType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.SHOOTER_METHOD_TYPE, Identifier.ofVanilla(id));
    }
}
