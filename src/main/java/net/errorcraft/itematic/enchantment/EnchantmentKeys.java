package net.errorcraft.itematic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EnchantmentKeys {
    public static final RegistryKey<Enchantment> UNBREAKING = of("unbreaking");
    public static final RegistryKey<Enchantment> MENDING = of("mending");
    public static final RegistryKey<Enchantment> VANISHING_CURSE = of("vanishing_curse");

    private EnchantmentKeys() {}

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, new Identifier(id));
    }
}
