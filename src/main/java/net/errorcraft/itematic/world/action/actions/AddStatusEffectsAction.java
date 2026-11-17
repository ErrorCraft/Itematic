package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.List;
import java.util.Optional;

public record AddStatusEffectsAction(LootContext.EntityTarget entity, List<StatusEffect> effects) implements Action<AddStatusEffectsAction> {
    public static final MapCodec<AddStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(AddStatusEffectsAction::entity),
        StatusEffect.CODEC.listOf().fieldOf("effects").forGetter(AddStatusEffectsAction::effects)
    ).apply(instance, AddStatusEffectsAction::new));

    public static AddStatusEffectsAction of(StatusEffect... effects) {
        return new AddStatusEffectsAction(
            LootContext.EntityTarget.THIS,
            List.of(effects)
        );
    }

    @Override
    public ActionType<AddStatusEffectsAction> type() {
        return ActionType.ADD_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        LootContext lootContext = context.lootContext();
        if (lootContext == null) {
            return false;
        }

        if (context.get(this.entity.contextParam()) instanceof LivingEntity target) {
            return this.addStatusEffects(target, lootContext);
        }

        return false;
    }

    private boolean addStatusEffects(LivingEntity target, LootContext context) {
        boolean addedStatusEffects = false;
        for (StatusEffect effect : this.effects) {
            addedStatusEffects |= target.addEffect(effect.toInstance(context));
        }

        return addedStatusEffects;
    }

    public record StatusEffect(Holder<MobEffect> effect, ResolvableInt amplifier, ResolvableInt duration, boolean ambient, boolean showParticles, Optional<Boolean> showIcon) {
        private static final ResolvableInt DEFAULT_AMPLIFIER = new ResolvableInt.Constant(0);
        private static final ResolvableInt DEFAULT_DURATION = new ResolvableInt.Constant(0);
        public static final Codec<StatusEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MobEffect.CODEC.fieldOf("id").forGetter(StatusEffect::effect),
            ResolvableInt.CODEC.optionalFieldOf("amplifier", DEFAULT_AMPLIFIER).forGetter(StatusEffect::amplifier),
            ResolvableInt.CODEC.optionalFieldOf("duration", DEFAULT_DURATION).forGetter(StatusEffect::duration),
            Codec.BOOL.optionalFieldOf("ambient", false).forGetter(StatusEffect::ambient),
            Codec.BOOL.optionalFieldOf("show_particles", true).forGetter(StatusEffect::showParticles),
            Codec.BOOL.optionalFieldOf("show_icon").forGetter(StatusEffect::showIcon)
        ).apply(instance, StatusEffect::new));

        public static StatusEffect of(Holder<MobEffect> effect, int duration) {
            return new StatusEffect(
                effect,
                DEFAULT_AMPLIFIER,
                new ResolvableInt.Constant(duration),
                false,
                true,
                Optional.empty()
            );
        }

        public static StatusEffect of(Holder<MobEffect> effect, int duration, int amplifier) {
            return new StatusEffect(
                effect,
                new ResolvableInt.Constant(amplifier),
                new ResolvableInt.Constant(duration),
                false,
                true,
                Optional.empty()
            );
        }

        public static StatusEffect of(Holder<MobEffect> effect, int duration, ResourceKey<ContextIntProvider> amplifier) {
            return new StatusEffect(
                effect,
                new ResolvableInt.Reference(amplifier),
                new ResolvableInt.Constant(duration),
                false,
                true,
                Optional.empty()
            );
        }

        public MobEffectInstance toInstance(LootContext context) {
            return new MobEffectInstance(
                this.effect,
                this.duration.get(context, 0),
                this.amplifier.get(context, 0),
                this.ambient,
                this.showParticles,
                this.showIcon.orElse(this.showParticles)
            );
        }
    }
}
