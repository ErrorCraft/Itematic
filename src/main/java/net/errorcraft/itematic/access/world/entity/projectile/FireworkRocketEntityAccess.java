package net.errorcraft.itematic.access.world.entity.projectile;

import net.errorcraft.itematic.world.item.component.GliderFuel;

public interface FireworkRocketEntityAccess {
    default void itematic$setGliderFuel(GliderFuel.Computed gliderFuel) {}
}
