package net.errorcraft.itematic.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class StatusEffectKeys {
    public static final RegistryKey<StatusEffect> JUMP_BOOST = of("jump_boost");
    public static final RegistryKey<StatusEffect> REGENERATION = of("regeneration");
    public static final RegistryKey<StatusEffect> FIRE_RESISTANCE = of("fire_resistance");
    public static final RegistryKey<StatusEffect> BLINDNESS = of("blindness");
    public static final RegistryKey<StatusEffect> NIGHT_VISION = of("night_vision");
    public static final RegistryKey<StatusEffect> WEAKNESS = of("weakness");
    public static final RegistryKey<StatusEffect> POISON = of("poison");
    public static final RegistryKey<StatusEffect> ABSORPTION = of("absorption");
    public static final RegistryKey<StatusEffect> SATURATION = of("saturation");

    private static RegistryKey<StatusEffect> of(String id) {
        return RegistryKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(id));
    }
}
