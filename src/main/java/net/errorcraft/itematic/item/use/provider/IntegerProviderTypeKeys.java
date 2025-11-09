package net.errorcraft.itematic.item.use.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class IntegerProviderTypeKeys {
    public static final RegistryKey<IntegerProviderType<?>> CONSTANT = of("constant");

    private IntegerProviderTypeKeys() {}

    private static RegistryKey<IntegerProviderType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE, new Identifier(id));
    }
}
