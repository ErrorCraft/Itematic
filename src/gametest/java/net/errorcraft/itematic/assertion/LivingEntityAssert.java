package net.errorcraft.itematic.assertion;

import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;

import java.util.List;
import java.util.function.Consumer;

public class LivingEntityAssert<E extends LivingEntity> extends BaseEntityAssert<LivingEntityAssert<E>, E> {
    LivingEntityAssert(TestContext helper, E entity) {
        super(helper, entity);
    }

    public LivingEntityAssert<E> hasHealth(Consumer<FloatsAssert> healthAssertion) {
        healthAssertion.accept(Assert.floats(this.helper, this.entity.getHealth(), "entity health"));
        return this;
    }

    public LivingEntityAssert<E> hasStackInHand(Hand hand, Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getStackInHand(hand), "item stack in hand"));
        return this;
    }

    public LivingEntityAssert<E> hasEquippedStack(EquipmentSlot slot, Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getEquippedStack(slot), "equipped item stack"));
        return this;
    }

    public LivingEntityAssert<E> hasEffect(RegistryEntry<StatusEffect> effect) {
        if (this.entity.hasStatusEffect(effect)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.entity.expected_effect",
            this.entity.getName(),
            PotionContentsComponent.getEffectText(effect, 0)
        );
    }

    public LivingEntityAssert<E> hasEffect(RegistryEntry<StatusEffect> effect, int amplifier) {
        StatusEffectInstance effectInstance = this.entity.getStatusEffect(effect);
        if (effectInstance != null && effectInstance.getAmplifier() == amplifier) {
            return this;
        }

        throw this.helper.createError(
            "test.error.entity.expected_effect",
            this.entity.getName(),
            PotionContentsComponent.getEffectText(effect, amplifier)
        );
    }

    public LivingEntityAssert<E> doesNotHaveEffect(RegistryEntry<StatusEffect> effect) {
        if (!this.entity.hasStatusEffect(effect)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.entity.did_not_expect_effect",
            this.entity.getName(),
            PotionContentsComponent.getEffectText(effect, 0)
        );
    }

    public LivingEntityAssert<E> hasEffects(RegistryEntry<Potion> potion) {
        for (StatusEffectInstance effect : potion.value().getEffects()) {
            this.hasEffect(effect.getEffectType(), effect.getAmplifier());
        }

        return this;
    }

    public LivingEntityAssert<E> hasEffects(List<SuspiciousStewEffectsComponent.StewEffect> effects) {
        for (SuspiciousStewEffectsComponent.StewEffect effect : effects) {
            this.hasEffect(effect.effect(), effect.createStatusEffectInstance().getAmplifier());
        }

        return this;
    }
}
