package net.errorcraft.itematic.world.item.component;

import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.UseCooldown;

import java.util.Optional;

public class UseCooldowns {
    private UseCooldowns() {}

    public static UseCooldown of(UseDurationProvider ticks) {
        return create(ticks, Optional.empty());
    }

    public static UseCooldown create(UseDurationProvider ticks, Optional<Identifier> cooldownGroup) {
        UseCooldown useCooldown = new UseCooldown(0.0f, cooldownGroup);
        useCooldown.itematic$setTicks(ticks);
        return useCooldown;
    }
}
