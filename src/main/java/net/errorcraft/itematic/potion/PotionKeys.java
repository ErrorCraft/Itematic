package net.errorcraft.itematic.potion;

import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class PotionKeys {
    public static final RegistryKey<Potion> WATER = of("water");

    private PotionKeys() {}

    private static RegistryKey<Potion> of(String id) {
        return RegistryKey.of(RegistryKeys.POTION, new Identifier(id));
    }
}
