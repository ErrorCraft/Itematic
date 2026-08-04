package net.errorcraft.itematic.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class FluidKeys {
    public static final ResourceKey<Fluid> WATER = of("water");
    public static final ResourceKey<Fluid> LAVA = of("lava");

    private FluidKeys() {}

    private static ResourceKey<Fluid> of(String id) {
        return ResourceKey.create(Registries.FLUID, Identifier.withDefaultNamespace(id));
    }
}
