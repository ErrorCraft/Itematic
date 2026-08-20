package net.errorcraft.itematic.references;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.waypoints.WaypointStyleAsset;

public class ItemBarStyleIds {
    ResourceKey<? extends Registry<WaypointStyleAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("waypoint_style_asset"));
    public static final Identifier DAMAGE = of("damage");
    public static final Identifier BUNDLE = of("bundle");

    private ItemBarStyleIds() {}

    private static Identifier of(String name) {
        return Identifier.withDefaultNamespace(name);
    }
}
