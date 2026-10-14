package net.errorcraft.itematic.core.component.provider.number.floats;

import net.errorcraft.itematic.core.component.provider.number.floats.providers.DamageProvider;
import net.errorcraft.itematic.core.component.provider.number.floats.providers.FoodProvider;
import net.minecraft.core.Registry;

public class FloatProviders {
    private FloatProviders() {}

    public static void bootstrap(Registry<FloatProvider.Type<?, ?>> registry) {
        Registry.register(registry, "food", FoodProvider.TYPE);
        Registry.register(registry, "damage", DamageProvider.TYPE);
    }
}
