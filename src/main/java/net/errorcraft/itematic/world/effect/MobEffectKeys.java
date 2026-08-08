package net.errorcraft.itematic.world.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

public class MobEffectKeys {
    public static final ResourceKey<MobEffect> ABSORPTION = of("absorption");
    public static final ResourceKey<MobEffect> BLINDNESS = of("blindness");
    public static final ResourceKey<MobEffect> FIRE_RESISTANCE = of("fire_resistance");
    public static final ResourceKey<MobEffect> HUNGER = of("hunger");
    public static final ResourceKey<MobEffect> JUMP_BOOST = of("jump_boost");
    public static final ResourceKey<MobEffect> NAUSEA = of("nausea");
    public static final ResourceKey<MobEffect> NIGHT_VISION = of("night_vision");
    public static final ResourceKey<MobEffect> POISON = of("poison");
    public static final ResourceKey<MobEffect> REGENERATION = of("regeneration");
    public static final ResourceKey<MobEffect> RESISTANCE = of("resistance");
    public static final ResourceKey<MobEffect> SATURATION = of("saturation");
    public static final ResourceKey<MobEffect> WEAKNESS = of("weakness");
    public static final ResourceKey<MobEffect> WITHER = of("wither");

    private MobEffectKeys() {}

    private static ResourceKey<MobEffect> of(String id) {
        return ResourceKey.create(Registries.MOB_EFFECT, Identifier.withDefaultNamespace(id));
    }
}
