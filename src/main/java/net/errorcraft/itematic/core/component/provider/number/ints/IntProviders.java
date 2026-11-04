package net.errorcraft.itematic.core.component.provider.number.ints;

import net.errorcraft.itematic.core.component.provider.number.ints.providers.CustomDataProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.DamageProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.EnchantmentLevelProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.FoodProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.MaxDamageProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.MaxStackSizeProvider;
import net.errorcraft.itematic.core.component.provider.number.ints.providers.OminousBottleAmplifierProvider;
import net.minecraft.core.Registry;

public class IntProviders {
    private IntProviders() {}

    public static void bootstrap(Registry<IntProvider.Type<?, ?>> registry) {
        Registry.register(registry, "max_stack_size", MaxStackSizeProvider.TYPE);
        Registry.register(registry, "max_damage", MaxDamageProvider.TYPE);
        Registry.register(registry, "damage", DamageProvider.TYPE);
        Registry.register(registry, "ominous_bottle_amplifier", OminousBottleAmplifierProvider.TYPE);
        Registry.register(registry, "food", FoodProvider.TYPE);
        Registry.register(registry, "enchantments", EnchantmentLevelProvider.Enchantments.TYPE);
        Registry.register(registry, "stored_enchantments", EnchantmentLevelProvider.StoredEnchantments.TYPE);
        Registry.register(registry, "custom_data", CustomDataProvider.TYPE);
    }
}
