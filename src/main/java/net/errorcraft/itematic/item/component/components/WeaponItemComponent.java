package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.WeaponAttackDamageDataComponent;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponComponents;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.errorcraft.itematic.item.weapon.melee.component.KineticMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.SmashingMeleeWeapon;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.component.type.SwingAnimationComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public record WeaponItemComponent(int itemDamagePerAttack, float disableBlockingForSeconds, ComponentMap types, Optional<RegistryEntry<DamageType>> damageType, Optional<SwingAnimationComponent> swingAnimation, WeaponAttackDamageDataComponent attackDamage, double attackSpeed, Optional<AttackRangeComponent> attackRange, Optional<Float> minimumAttackCharge) implements ItemComponent<WeaponItemComponent>, ComponentHolder {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(WeaponItemComponent::itemDamagePerAttack),
        Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("disable_blocking_for_seconds", 0.0f).forGetter(WeaponItemComponent::disableBlockingForSeconds),
        MeleeWeaponComponents.CODEC.optionalFieldOf("types", ComponentMap.EMPTY).forGetter(WeaponItemComponent::types),
        DamageType.ENTRY_CODEC.optionalFieldOf("damage_type").forGetter(WeaponItemComponent::damageType),
        SwingAnimationComponent.CODEC.optionalFieldOf("swing_animation").forGetter(WeaponItemComponent::swingAnimation),
        WeaponAttackDamageDataComponent.CODEC.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed),
        AttackRangeComponent.CODEC.optionalFieldOf("attack_range").forGetter(WeaponItemComponent::attackRange),
        Codecs.rangedInclusiveFloat(0.0f, 1.0f).optionalFieldOf("minimum_attack_charge").forGetter(WeaponItemComponent::minimumAttackCharge)
    ).apply(instance, WeaponItemComponent::new));

    public static Builder builder(int itemDamagePerAttack, double attackDamage, double attackSpeed) {
        return new Builder(itemDamagePerAttack, attackDamage, attackSpeed);
    }

    @Override
    public ItemComponentType<WeaponItemComponent> type() {
        return ItemComponentTypes.WEAPON;
    }

    @Override
    public Codec<WeaponItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, ItemStackExchanger stackExchanger) {
        SmashingMeleeWeapon smashing = this.types.get(MeleeWeaponComponents.SMASHING);
        if (smashing != null) {
            smashing.hit(stack, target, attacker);
        }

        Hand usedHand = attacker.getActiveHand();
        ActionContext context = ActionContext.builder(attacker.getEntityWorld())
            .stackExchanger(stackExchanger)
            .add(LootContextParameters.ATTACKING_ENTITY, attacker)
            .add(LootContextParameters.ORIGIN, attacker.getEntityPos())
            .add(LootContextParameters.TARGET_ENTITY, target)
            .add(ItematicContextParameters.INTERACTED_POSITION, target.getEntityPos())
            .add(LootContextParameters.TOOL, stack)
            .add(ItematicContextParameters.HAND, usedHand)
            .add(ItematicContextParameters.EQUIPMENT_SLOT, usedHand.getEquipmentSlot())
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        WeaponComponent weapon = stack.get(DataComponentTypes.WEAPON);
        if (weapon != null) {
            stack.itematic$damage(weapon.itemDamagePerAttack(), context);
        }
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        KineticMeleeWeapon kinetic = this.types.get(MeleeWeaponComponents.KINETIC);
        if (kinetic != null) {
            kinetic.hold(stack, world, user, usedTicks);
        }
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.WEAPON, new WeaponComponent(this.itemDamagePerAttack, this.disableBlockingForSeconds));
        builder.add(ItematicDataComponentTypes.WEAPON_ATTACK_DAMAGE, this.attackDamage);
        builder.add(ItematicDataComponentTypes.ATTACK_SPEED_MULTIPLIER, this.attackSpeed);
        this.streamAll(MeleeWeaponWithDataComponents.class)
            .forEach(meleeWeapon -> meleeWeapon.addComponents(builder));
        this.damageType.ifPresent(damageType -> builder.add(DataComponentTypes.DAMAGE_TYPE, new LazyRegistryEntryReference<>(damageType)));
        this.swingAnimation.ifPresent(swingAnimation -> builder.add(DataComponentTypes.SWING_ANIMATION, swingAnimation));
        this.attackRange.ifPresent(attackRange -> builder.add(DataComponentTypes.ATTACK_RANGE, attackRange));
        this.minimumAttackCharge.ifPresent(minimumAttackCharge -> builder.add(DataComponentTypes.MINIMUM_ATTACK_CHARGE, minimumAttackCharge));
    }

    @Override
    public ComponentMap getComponents() {
        return this.types;
    }

    public float bonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        SmashingMeleeWeapon smashing = this.types.get(MeleeWeaponComponents.SMASHING);
        if (smashing != null) {
            return smashing.bonusAttackDamage(target, baseAttackDamage, damageSource);
        }

        return 0.0f;
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        SmashingMeleeWeapon smashing = this.types.get(MeleeWeaponComponents.SMASHING);
        if (smashing != null) {
            smashing.postDamageEntity(stack, target, attacker);
        }
    }

    public DamageSource damageSource(ItemStack stack, LivingEntity attacker) {
        SmashingMeleeWeapon smashing = this.types.get(MeleeWeaponComponents.SMASHING);
        if (smashing != null) {
            return smashing.damageSource(stack, attacker);
        }

        return null;
    }

    public static class Builder {
        private final int itemDamagePerAttack;
        private final ComponentMap.Builder types = ComponentMap.builder();
        private RegistryEntry<DamageType> damageType;
        private SwingAnimationComponent swingAnimation;
        private final double attackDamage;
        private final double attackSpeed;
        private AttackRangeComponent attackRange;
        private Float minimumAttackCharge;
        private float disableBlockingForSeconds;

        private Builder(int itemDamagePerAttack, double attackDamage, double attackSpeed) {
            this.itemDamagePerAttack = itemDamagePerAttack;
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        public WeaponItemComponent build() {
            return new WeaponItemComponent(
                this.itemDamagePerAttack,
                this.disableBlockingForSeconds,
                this.types.build(),
                Optional.ofNullable(this.damageType),
                Optional.ofNullable(this.swingAnimation),
                new WeaponAttackDamageDataComponent(List.of(), this.attackDamage),
                this.attackSpeed,
                Optional.ofNullable(this.attackRange),
                Optional.ofNullable(this.minimumAttackCharge)
            );
        }

        public <T> Builder type(ComponentType<T> type, T value) {
            this.types.add(type, value);
            return this;
        }

        public Builder damageType(RegistryEntry<DamageType> damageType) {
            this.damageType = damageType;
            return this;
        }

        public Builder swingAnimation(SwingAnimationComponent swingAnimation) {
            this.swingAnimation = swingAnimation;
            return this;
        }

        public Builder attackRange(AttackRangeComponent attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public Builder minimumAttackCharge(float minimumAttackCharge) {
            this.minimumAttackCharge = minimumAttackCharge;
            return this;
        }

        public Builder disableBlockingForSeconds(float disableBlockingForSeconds) {
            this.disableBlockingForSeconds = disableBlockingForSeconds;
            return this;
        }
    }
}
