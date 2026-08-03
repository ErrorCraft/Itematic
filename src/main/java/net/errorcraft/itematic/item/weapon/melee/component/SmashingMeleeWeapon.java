package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.SmashingWeaponDataComponent;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;

public record SmashingMeleeWeapon(SmashingWeaponDataComponent smashingWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<SmashingMeleeWeapon> CODEC = SmashingWeaponDataComponent.CODEC.xmap(
        SmashingMeleeWeapon::new,
        SmashingMeleeWeapon::smashingWeapon
    );
    private static final MaceItem DUMMY = new MaceItem(new Item.Settings());

    public static SmashingMeleeWeapon of(SmashingWeaponDataComponent smashingWeapon) {
        return new SmashingMeleeWeapon(smashingWeapon);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(ItematicDataComponentTypes.SMASHING_WEAPON, this.smashingWeapon);
    }

    public void hit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DUMMY.postHit(stack, target, attacker);
    }

    public float bonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return DUMMY.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DUMMY.postDamageEntity(stack, target, attacker);
    }

    public DamageSource damageSource(ItemStack stack, LivingEntity attacker) {
        SmashingWeaponDataComponent smashingWeapon = stack.get(ItematicDataComponentTypes.SMASHING_WEAPON);
        if (smashingWeapon != null) {
            return smashingWeapon.damageSource(attacker);
        }

        return null;
    }
}
