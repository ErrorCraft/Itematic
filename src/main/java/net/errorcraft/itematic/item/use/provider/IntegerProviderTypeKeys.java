package net.errorcraft.itematic.item.use.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class IntegerProviderTypeKeys {
    public static final RegistryKey<IntegerProviderType<?>> CONSTANT = of("constant");
    public static final RegistryKey<IntegerProviderType<?>> PLAYABLE = of("playable");
    public static final RegistryKey<IntegerProviderType<?>> SHOOTER = of("shooter");
    public static final RegistryKey<IntegerProviderType<?>> TRIDENT = of("trident");
    public static final RegistryKey<IntegerProviderType<?>> CONDITION = of("condition");

    private IntegerProviderTypeKeys() {}

    private static RegistryKey<IntegerProviderType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE, Identifier.ofVanilla(id));
    }
}
