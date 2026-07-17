package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public record KineticMeleeWeapon(KineticWeaponComponent kineticWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<KineticMeleeWeapon> CODEC = KineticWeaponComponent.CODEC.xmap(
        KineticMeleeWeapon::new,
        KineticMeleeWeapon::kineticWeapon
    );

    public static KineticMeleeWeapon of(KineticWeaponComponent kineticWeapon) {
        return new KineticMeleeWeapon(kineticWeapon);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.KINETIC_WEAPON, this.kineticWeapon);
    }

    public void hold(ItemStack stack, World world, LivingEntity user, int usedTicks) {
        if (world.isClient()) {
            return;
        }

        KineticWeaponComponent kineticWeapon = stack.get(DataComponentTypes.KINETIC_WEAPON);
        if (kineticWeapon == null) {
            return;
        }

        kineticWeapon.usageTick(stack, usedTicks, user, user.getActiveHand().getEquipmentSlot());
    }
}
