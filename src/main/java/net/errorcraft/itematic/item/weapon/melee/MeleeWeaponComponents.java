package net.errorcraft.itematic.item.weapon.melee;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.weapon.melee.component.DisablesBlockingMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.KineticMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.PiercingMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.SmashingMeleeWeapon;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class MeleeWeaponComponents {
    public static final Codec<ComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(ItematicRegistries.MELEE_WEAPON_COMPONENT_TYPE::getCodec);
    public static final Codec<ComponentMap> CODEC = ComponentMap.createCodec(COMPONENT_CODEC);
    public static final ComponentType<SmashingMeleeWeapon> SMASHING = register("smashing", builder -> builder.codec(SmashingMeleeWeapon.CODEC));
    public static final ComponentType<KineticMeleeWeapon> KINETIC = register("kinetic", builder -> builder.codec(KineticMeleeWeapon.CODEC));
    public static final ComponentType<PiercingMeleeWeapon> PIERCING = register("piercing", builder -> builder.codec(PiercingMeleeWeapon.CODEC));
    public static final ComponentType<DisablesBlockingMeleeWeapon> DISABLES_BLOCKING = register("disables_blocking", builder -> builder.codec(DisablesBlockingMeleeWeapon.CODEC));

    private MeleeWeaponComponents() {}

    public static void init() {}

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(ItematicRegistries.MELEE_WEAPON_COMPONENT_TYPE, id, builder.apply(ComponentType.builder()).build());
    }
}
