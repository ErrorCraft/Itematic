package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.errorcraft.itematic.world.item.component.SmashingWeapon;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;

public record SmashingMeleeWeapon(SmashingWeapon smashingWeapon) implements MeleeWeaponWithDataComponents {
    public static final Codec<SmashingMeleeWeapon> CODEC = SmashingWeapon.CODEC.xmap(
        SmashingMeleeWeapon::new,
        SmashingMeleeWeapon::smashingWeapon
    );
    private static final MaceItem DUMMY = new MaceItem(new Item.Properties());

    public static SmashingMeleeWeapon of(SmashingWeapon smashingWeapon) {
        return new SmashingMeleeWeapon(smashingWeapon);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SMASHING_WEAPON, this.smashingWeapon);
    }

    public void hit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DUMMY.hurtEnemy(stack, target, attacker);
    }

    public float bonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return DUMMY.getAttackDamageBonus(target, baseAttackDamage, damageSource);
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DUMMY.postHurtEnemy(stack, target, attacker);
    }

    public DamageSource damageSource(ItemStack stack, LivingEntity attacker) {
        SmashingWeapon smashingWeapon = stack.get(ItematicDataComponents.SMASHING_WEAPON);
        if (smashingWeapon != null) {
            return smashingWeapon.damageSource(attacker);
        }

        return null;
    }
}
