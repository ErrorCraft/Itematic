package net.errorcraft.itematic.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FluidKeys {
    public static final RegistryKey<Fluid> EMPTY = of("empty");
    public static final RegistryKey<Fluid> WATER = of("water");
    public static final RegistryKey<Fluid> LAVA = of("lava");

    private FluidKeys() {}

    private static RegistryKey<Fluid> of(String id) {
        return RegistryKey.of(RegistryKeys.FLUID, Identifier.ofVanilla(id));
    }
}
