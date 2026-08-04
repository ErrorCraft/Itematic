package net.errorcraft.itematic.item.use.provider;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class IntegerProviderTypeKeys {
    public static final ResourceKey<IntegerProviderType<?>> CONSTANT = of("constant");
    public static final ResourceKey<IntegerProviderType<?>> PLAYABLE = of("playable");
    public static final ResourceKey<IntegerProviderType<?>> SHOOTER = of("shooter");
    public static final ResourceKey<IntegerProviderType<?>> TRIDENT = of("trident");
    public static final ResourceKey<IntegerProviderType<?>> CONDITION = of("condition");
    public static final ResourceKey<IntegerProviderType<?>> INDEFINITE = of("indefinite");

    private IntegerProviderTypeKeys() {}

    private static ResourceKey<IntegerProviderType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.INTEGER_PROVIDER_TYPE, Identifier.withDefaultNamespace(id));
    }
}
