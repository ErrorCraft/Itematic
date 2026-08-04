package net.errorcraft.itematic.item.shooter.method;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ShooterMethodTypeKeys {
    public static final ResourceKey<ShooterMethodType<?>> DIRECT = of("direct");
    public static final ResourceKey<ShooterMethodType<?>> CHARGEABLE = of("chargeable");

    private ShooterMethodTypeKeys() {}

    private static ResourceKey<ShooterMethodType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.SHOOTER_METHOD_TYPE, Identifier.withDefaultNamespace(id));
    }
}
