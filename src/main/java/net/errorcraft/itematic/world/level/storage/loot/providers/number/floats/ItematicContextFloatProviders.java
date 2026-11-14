package net.errorcraft.itematic.world.level.storage.loot.providers.number.floats;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class ItematicContextFloatProviders {
    public static final ResourceKey<ContextFloatProvider> GLIDER_FIREWORK_ROCKET_CONSTANTLY_ADDED_SPEED = createKey("glider/firework_rocket_constantly_added_speed");
    public static final ResourceKey<ContextFloatProvider> GLIDER_FIREWORK_ROCKET_TARGET_SPEED = createKey("glider/firework_rocket_target_speed");
    public static final ResourceKey<ContextFloatProvider> GLIDER_FIREWORK_ROCKET_TARGET_SPEED_FACTOR = createKey("glider/firework_rocket_target_speed_factor");

    private ItematicContextFloatProviders() {}

    public static void bootstrap(BootstrapContext<ContextFloatProvider> context) {
        context.register(
            GLIDER_FIREWORK_ROCKET_CONSTANTLY_ADDED_SPEED,
            new ConstantValue(0.1f)
        );
        context.register(
            GLIDER_FIREWORK_ROCKET_TARGET_SPEED,
            new ConstantValue(1.5f)
        );
        context.register(
            GLIDER_FIREWORK_ROCKET_TARGET_SPEED_FACTOR,
            new ConstantValue(0.5f)
        );
    }

    private static ResourceKey<ContextFloatProvider> createKey(String name) {
        return ResourceKey.create(Registries.CONTEXT_FLOAT_PROVIDER, Identifier.withDefaultNamespace(name));
    }
}
