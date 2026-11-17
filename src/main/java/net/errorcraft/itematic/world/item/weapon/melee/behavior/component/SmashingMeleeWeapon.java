package net.errorcraft.itematic.world.item.weapon.melee.behavior.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.weapon.melee.SmashingWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponWithDataComponents;
import net.minecraft.core.component.DataComponentMap;

public record SmashingMeleeWeapon(SmashingWeapon smashingWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<SmashingMeleeWeapon> CODEC = SmashingWeapon.CODEC.xmap(
        SmashingMeleeWeapon::new,
        SmashingMeleeWeapon::smashingWeapon
    );

    public static SmashingMeleeWeapon of(SmashingWeapon smashingWeapon) {
        return new SmashingMeleeWeapon(smashingWeapon);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SMASHING_WEAPON, this.smashingWeapon);
    }
}
