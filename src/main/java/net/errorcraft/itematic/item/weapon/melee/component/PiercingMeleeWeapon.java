package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PiercingWeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public record PiercingMeleeWeapon(PiercingWeaponComponent piercingWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<PiercingMeleeWeapon> CODEC = PiercingWeaponComponent.CODEC.xmap(
        PiercingMeleeWeapon::new,
        PiercingMeleeWeapon::piercingWeapon
    );

    public static PiercingMeleeWeapon of(PiercingWeaponComponent piercingWeapon) {
        return new PiercingMeleeWeapon(piercingWeapon);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.PIERCING_WEAPON, this.piercingWeapon);
    }

    public void pierce(ItemStack stack, ServerPlayerEntity attacker, EquipmentSlot slot) {
        PiercingWeaponComponent piercingWeapon = stack.get(DataComponentTypes.PIERCING_WEAPON);
        if (piercingWeapon == null) {
            return;
        }

        piercingWeapon.stab(attacker, slot);
    }
}
