package net.errorcraft.itematic.world.item.weapon.melee.behavior.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponWithDataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.level.Level;

public record KineticMeleeWeapon(KineticWeapon kineticWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<KineticMeleeWeapon> CODEC = KineticWeapon.CODEC.xmap(
        KineticMeleeWeapon::new,
        KineticMeleeWeapon::kineticWeapon
    );

    public static KineticMeleeWeapon of(KineticWeapon kineticWeapon) {
        return new KineticMeleeWeapon(kineticWeapon);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.KINETIC_WEAPON, this.kineticWeapon);
    }

    public void hold(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        if (level.isClientSide()) {
            return;
        }

        KineticWeapon kineticWeapon = stack.get(DataComponents.KINETIC_WEAPON);
        if (kineticWeapon == null) {
            return;
        }

        kineticWeapon.damageEntities(stack, usedTicks, user, user.getUsedItemHand().asEquipmentSlot());
    }
}
