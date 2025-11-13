package net.errorcraft.itematic.item.use.provider;

import net.errorcraft.itematic.item.use.provider.providers.ConstantIntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.PlayableIntegerProvider;
import net.errorcraft.itematic.item.use.provider.providers.ShooterIntegerProvider;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class IntegerProviderTypes {
    public static final IntegerProviderType<ConstantIntegerProvider> CONSTANT = register(IntegerProviderTypeKeys.CONSTANT, new IntegerProviderType<>(ConstantIntegerProvider.CODEC, ConstantIntegerProvider.PACKET_CODEC));
    public static final IntegerProviderType<PlayableIntegerProvider> PLAYABLE = register(IntegerProviderTypeKeys.PLAYABLE, new IntegerProviderType<>(PlayableIntegerProvider.CODEC, PlayableIntegerProvider.PACKET_CODEC));
    public static final IntegerProviderType<ShooterIntegerProvider> SHOOTER = register(IntegerProviderTypeKeys.SHOOTER, new IntegerProviderType<>(ShooterIntegerProvider.CODEC, ShooterIntegerProvider.PACKET_CODEC));

    private IntegerProviderTypes() {}

    public static void init() {}

    private static <T extends IntegerProvider> IntegerProviderType<T> register(RegistryKey<IntegerProviderType<?>> id, IntegerProviderType<T> type) {
        return Registry.register(ItematicRegistries.INTEGER_PROVIDER_TYPE, id, type);
    }
}
