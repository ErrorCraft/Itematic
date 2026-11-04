package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.melee.WeaponAttackDamage;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponWithDataComponents;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.DisablesBlockingMeleeWeapon;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.Weapon;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record WeaponItemBehavior(int itemDamagePerAttack, DataComponentMap types, Optional<Holder<DamageType>> damageType, Optional<SwingAnimation> attackAnimation, WeaponAttackDamage attackDamage, double attackSpeed, Optional<AttackRange> attackRange, Optional<Float> minimumAttackCharge) implements ItemBehavior<WeaponItemBehavior> {
    public static final Codec<WeaponItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(WeaponItemBehavior::itemDamagePerAttack),
        MeleeWeaponComponents.CODEC.optionalFieldOf("types", DataComponentMap.EMPTY).forGetter(WeaponItemBehavior::types),
        DamageType.CODEC.optionalFieldOf("damage_type").forGetter(WeaponItemBehavior::damageType),
        SwingAnimation.CODEC.optionalFieldOf("attack_animation").forGetter(WeaponItemBehavior::attackAnimation),
        WeaponAttackDamage.CODEC.fieldOf("attack_damage").forGetter(WeaponItemBehavior::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemBehavior::attackSpeed),
        AttackRange.CODEC.optionalFieldOf("attack_range").forGetter(WeaponItemBehavior::attackRange),
        ExtraCodecs.floatRange(0.0f, 1.0f).optionalFieldOf("minimum_attack_charge").forGetter(WeaponItemBehavior::minimumAttackCharge)
    ).apply(instance, WeaponItemBehavior::new));

    public static Builder builder(int itemDamagePerAttack, double attackDamage, double attackSpeed) {
        return new Builder(itemDamagePerAttack, attackDamage, attackSpeed);
    }

    @Override
    public ItemBehaviorType<WeaponItemBehavior> type() {
        return ItemBehaviorType.WEAPON;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        DisablesBlockingMeleeWeapon disablesBlocking = this.types.get(MeleeWeaponComponents.DISABLES_BLOCKING);
        builder.set(
            DataComponents.WEAPON,
            new Weapon(
                this.itemDamagePerAttack,
                disablesBlocking != null ? disablesBlocking.seconds() : 0.0f
            )
        );
        builder.set(ItematicDataComponents.WEAPON_ATTACK_DAMAGE, this.attackDamage);
        builder.set(ItematicDataComponents.ATTACK_SPEED_MULTIPLIER, this.attackSpeed);
        this.typeComponents()
            .getAllOfType(MeleeWeaponWithDataComponents.class)
            .forEach(meleeWeapon -> meleeWeapon.addComponents(builder));
        this.damageType.ifPresent(damageType -> builder.set(DataComponents.DAMAGE_TYPE, damageType));
        this.attackAnimation.ifPresent(attackAnimation -> builder.set(DataComponents.ATTACK_ANIMATION, attackAnimation));
        this.attackRange.ifPresent(attackRange -> builder.set(DataComponents.ATTACK_RANGE, attackRange));
        this.minimumAttackCharge.ifPresent(minimumAttackCharge -> builder.set(DataComponents.MINIMUM_ATTACK_CHARGE, minimumAttackCharge));
    }

    private DataComponentHolder typeComponents() {
        return () -> this.types;
    }

    public static class Builder {
        private final int itemDamagePerAttack;
        private final DataComponentMap.Builder types = DataComponentMap.builder();
        @Nullable
        private Holder<DamageType> damageType;
        @Nullable
        private SwingAnimation attackAnimation;
        private final double attackDamage;
        private final double attackSpeed;
        @Nullable
        private AttackRange attackRange;
        @Nullable
        private Float minimumAttackCharge;

        private Builder(int itemDamagePerAttack, double attackDamage, double attackSpeed) {
            this.itemDamagePerAttack = itemDamagePerAttack;
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        public WeaponItemBehavior build() {
            return new WeaponItemBehavior(
                this.itemDamagePerAttack,
                this.types.build(),
                Optional.ofNullable(this.damageType),
                Optional.ofNullable(this.attackAnimation),
                new WeaponAttackDamage(List.of(), this.attackDamage),
                this.attackSpeed,
                Optional.ofNullable(this.attackRange),
                Optional.ofNullable(this.minimumAttackCharge)
            );
        }

        public <T> Builder type(DataComponentType<T> type, T value) {
            this.types.set(type, value);
            return this;
        }

        public Builder damageType(Holder<DamageType> damageType) {
            this.damageType = damageType;
            return this;
        }

        public Builder attackAnimation(SwingAnimation swingAnimation) {
            this.attackAnimation = swingAnimation;
            return this;
        }

        public Builder attackRange(AttackRange attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public Builder minimumAttackCharge(float minimumAttackCharge) {
            this.minimumAttackCharge = minimumAttackCharge;
            return this;
        }

        public Builder disableBlockingForSeconds(float disableBlockingForSeconds) {
            if (disableBlockingForSeconds > 0.0f) {
                this.type(MeleeWeaponComponents.DISABLES_BLOCKING, new DisablesBlockingMeleeWeapon(disableBlockingForSeconds));
            }

            return this;
        }
    }
}
