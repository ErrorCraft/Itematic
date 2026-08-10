package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponComponents;
import net.errorcraft.itematic.item.weapon.melee.MeleeWeaponWithDataComponents;
import net.errorcraft.itematic.item.weapon.melee.component.DisablesBlockingMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.KineticMeleeWeapon;
import net.errorcraft.itematic.item.weapon.melee.component.SmashingMeleeWeapon;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.component.WeaponAttackDamage;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record WeaponItemComponent(int itemDamagePerAttack, DataComponentMap types, Optional<Holder<DamageType>> damageType, Optional<SwingAnimation> swingAnimation, WeaponAttackDamage attackDamage, double attackSpeed, Optional<AttackRange> attackRange, Optional<Float> minimumAttackCharge) implements ItemComponent<WeaponItemComponent>, DataComponentHolder {
    public static final Codec<WeaponItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(WeaponItemComponent::itemDamagePerAttack),
        MeleeWeaponComponents.CODEC.optionalFieldOf("types", DataComponentMap.EMPTY).forGetter(WeaponItemComponent::types),
        DamageType.CODEC.optionalFieldOf("damage_type").forGetter(WeaponItemComponent::damageType),
        SwingAnimation.CODEC.optionalFieldOf("swing_animation").forGetter(WeaponItemComponent::swingAnimation),
        WeaponAttackDamage.CODEC.fieldOf("attack_damage").forGetter(WeaponItemComponent::attackDamage),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("attack_speed").forGetter(WeaponItemComponent::attackSpeed),
        AttackRange.CODEC.optionalFieldOf("attack_range").forGetter(WeaponItemComponent::attackRange),
        ExtraCodecs.floatRange(0.0f, 1.0f).optionalFieldOf("minimum_attack_charge").forGetter(WeaponItemComponent::minimumAttackCharge)
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

        InteractionHand usedHand = attacker.getUsedItemHand();
        ActionContext context = ActionContext.builder(attacker.level())
            .stackExchanger(stackExchanger)
            .add(LootContextParams.THIS_ENTITY, attacker)
            .add(LootContextParams.ATTACKING_ENTITY, attacker)
            .add(LootContextParams.ORIGIN, attacker.position())
            .add(LootContextParams.TARGET_ENTITY, target)
            .add(ItematicContextParameters.INTERACTED_POSITION, target.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextParameters.HAND, usedHand)
            .add(ItematicContextParameters.EQUIPMENT_SLOT, usedHand.asEquipmentSlot())
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_WEAPON, context);
        Weapon weapon = stack.get(DataComponents.WEAPON);
        if (weapon != null) {
            stack.itematic$damage(weapon.itemDamagePerAttack(), context);
        }
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        KineticMeleeWeapon kinetic = this.types.get(MeleeWeaponComponents.KINETIC);
        if (kinetic != null) {
            kinetic.hold(stack, world, user, usedTicks);
        }
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        DisablesBlockingMeleeWeapon disablesBlocking = this.types.get(MeleeWeaponComponents.DISABLES_BLOCKING);
        builder.set(DataComponents.WEAPON, new Weapon(
            this.itemDamagePerAttack,
            disablesBlocking != null ? disablesBlocking.seconds() : 0.0f
        ));
        builder.set(ItematicDataComponents.WEAPON_ATTACK_DAMAGE, this.attackDamage);
        builder.set(ItematicDataComponents.ATTACK_SPEED_MULTIPLIER, this.attackSpeed);
        this.getAllOfType(MeleeWeaponWithDataComponents.class)
            .forEach(meleeWeapon -> meleeWeapon.addComponents(builder));
        this.damageType.ifPresent(damageType -> builder.set(DataComponents.DAMAGE_TYPE, new EitherHolder<>(damageType)));
        this.swingAnimation.ifPresent(swingAnimation -> builder.set(DataComponents.SWING_ANIMATION, swingAnimation));
        this.attackRange.ifPresent(attackRange -> builder.set(DataComponents.ATTACK_RANGE, attackRange));
        this.minimumAttackCharge.ifPresent(minimumAttackCharge -> builder.set(DataComponents.MINIMUM_ATTACK_CHARGE, minimumAttackCharge));
    }

    @Override
    public DataComponentMap getComponents() {
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

    @Nullable
    public DamageSource damageSource(ItemStack stack, LivingEntity attacker) {
        SmashingMeleeWeapon smashing = this.types.get(MeleeWeaponComponents.SMASHING);
        if (smashing != null) {
            return smashing.damageSource(stack, attacker);
        }

        return null;
    }

    public static class Builder {
        private final int itemDamagePerAttack;
        private final DataComponentMap.Builder types = DataComponentMap.builder();
        private Holder<DamageType> damageType;
        private SwingAnimation swingAnimation;
        private final double attackDamage;
        private final double attackSpeed;
        private AttackRange attackRange;
        private Float minimumAttackCharge;

        private Builder(int itemDamagePerAttack, double attackDamage, double attackSpeed) {
            this.itemDamagePerAttack = itemDamagePerAttack;
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        public WeaponItemComponent build() {
            return new WeaponItemComponent(
                this.itemDamagePerAttack,
                this.types.build(),
                Optional.ofNullable(this.damageType),
                Optional.ofNullable(this.swingAnimation),
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

        public Builder swingAnimation(SwingAnimation swingAnimation) {
            this.swingAnimation = swingAnimation;
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
