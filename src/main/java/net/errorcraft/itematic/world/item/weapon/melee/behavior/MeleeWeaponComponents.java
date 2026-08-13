package net.errorcraft.itematic.world.item.weapon.melee.behavior;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.DisablesBlockingMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.KineticMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.PiercingMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.SmashingMeleeWeapon;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;

import java.util.function.UnaryOperator;

public class MeleeWeaponComponents {
    public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(ItematicRegistries.MELEE_WEAPON_COMPONENT_TYPE::byNameCodec);
    public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);
    public static final DataComponentType<SmashingMeleeWeapon> SMASHING = register("smashing", builder -> builder.persistent(SmashingMeleeWeapon.CODEC));
    public static final DataComponentType<KineticMeleeWeapon> KINETIC = register("kinetic", builder -> builder.persistent(KineticMeleeWeapon.CODEC));
    public static final DataComponentType<PiercingMeleeWeapon> PIERCING = register("piercing", builder -> builder.persistent(PiercingMeleeWeapon.CODEC));
    public static final DataComponentType<DisablesBlockingMeleeWeapon> DISABLES_BLOCKING = register("disables_blocking", builder -> builder.persistent(DisablesBlockingMeleeWeapon.CODEC));

    private MeleeWeaponComponents() {}

    public static void init() {}

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(ItematicRegistries.MELEE_WEAPON_COMPONENT_TYPE, id, builder.apply(DataComponentType.builder()).build());
    }
}
