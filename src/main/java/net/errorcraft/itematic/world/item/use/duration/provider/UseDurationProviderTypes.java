package net.errorcraft.itematic.world.item.use.duration.provider;

import net.errorcraft.itematic.world.item.use.duration.provider.providers.Condition;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Constant;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Indefinite;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Instant;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Playable;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Shooter;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Trident;
import net.minecraft.core.Registry;

public class UseDurationProviderTypes {
    private UseDurationProviderTypes() {}

    public static void bootstrap(Registry<UseDurationProvider.Type<?>> registry) {
        Registry.register(registry, "constant", Constant.TYPE);
        Registry.register(registry, "instant", Instant.TYPE);
        Registry.register(registry, "indefinite", Indefinite.TYPE);
        Registry.register(registry, "condition", Condition.TYPE);
        Registry.register(registry, "playable", Playable.TYPE);
        Registry.register(registry, "shooter", Shooter.TYPE);
        Registry.register(registry, "trident", Trident.TYPE);
    }
}
