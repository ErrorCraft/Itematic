package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;

public record PiercingMeleeWeapon(PiercingWeapon piercingWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<PiercingMeleeWeapon> CODEC = PiercingWeapon.CODEC.xmap(
        PiercingMeleeWeapon::new,
        PiercingMeleeWeapon::piercingWeapon
    );

    public static PiercingMeleeWeapon of(PiercingWeapon piercingWeapon) {
        return new PiercingMeleeWeapon(piercingWeapon);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.PIERCING_WEAPON, this.piercingWeapon);
    }

    public void pierce(ItemStack stack, ServerPlayer attacker, EquipmentSlot slot) {
        PiercingWeapon piercingWeapon = stack.get(DataComponents.PIERCING_WEAPON);
        if (piercingWeapon == null) {
            return;
        }

        piercingWeapon.attack(attacker, slot);
    }
}
