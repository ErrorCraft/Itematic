package net.errorcraft.itematic.assertion;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.List;
import java.util.function.Consumer;

public class LivingEntityAssert<E extends LivingEntity> extends BaseEntityAssert<LivingEntityAssert<E>, E> {
    LivingEntityAssert(GameTestHelper helper, E entity) {
        super(helper, entity);
    }

    public LivingEntityAssert<E> hasHealth(Consumer<FloatsAssert> healthAssertion) {
        healthAssertion.accept(Assert.floats(this.helper, this.entity.getHealth(), "entity health"));
        return this;
    }

    public LivingEntityAssert<E> hasStackInHand(InteractionHand hand, Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getItemInHand(hand), "item stack in hand"));
        return this;
    }

    public LivingEntityAssert<E> hasEquippedStack(EquipmentSlot slot, Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getItemBySlot(slot), "equipped item stack"));
        return this;
    }

    public LivingEntityAssert<E> hasEffect(Holder<MobEffect> effect) {
        if (this.entity.hasEffect(effect)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.entity.expected_effect",
            this.entity.getName(),
            PotionContents.getPotionDescription(effect, 0)
        );
    }

    public LivingEntityAssert<E> hasEffect(Holder<MobEffect> effect, int amplifier) {
        MobEffectInstance effectInstance = this.entity.getEffect(effect);
        if (effectInstance != null && effectInstance.getAmplifier() == amplifier) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.entity.expected_effect",
            this.entity.getName(),
            PotionContents.getPotionDescription(effect, amplifier)
        );
    }

    public LivingEntityAssert<E> doesNotHaveEffect(Holder<MobEffect> effect) {
        if (!this.entity.hasEffect(effect)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.entity.did_not_expect_effect",
            this.entity.getName(),
            PotionContents.getPotionDescription(effect, 0)
        );
    }

    public LivingEntityAssert<E> hasEffects(Holder<Potion> potion) {
        for (MobEffectInstance effect : potion.value().getEffects()) {
            this.hasEffect(effect.getEffect(), effect.getAmplifier());
        }

        return this;
    }

    public LivingEntityAssert<E> hasEffects(List<SuspiciousStewEffects.Entry> effects) {
        for (SuspiciousStewEffects.Entry effect : effects) {
            this.hasEffect(effect.effect(), effect.createEffectInstance().getAmplifier());
        }

        return this;
    }
}
