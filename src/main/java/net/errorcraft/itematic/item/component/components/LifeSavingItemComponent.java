package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.effect.StatusEffectInstanceUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public record LifeSavingItemComponent(List<StatusEffectInstance> effects) implements ItemComponent<LifeSavingItemComponent> {
    public static final Codec<LifeSavingItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StatusEffectInstanceUtil.CODEC.listOf().fieldOf("effects").forGetter(LifeSavingItemComponent::effects)
    ).apply(instance, LifeSavingItemComponent::new));

    @Override
    public ItemComponentType<LifeSavingItemComponent> type() {
        return ItemComponentTypes.LIFE_SAVING;
    }

    @Override
    public Codec<LifeSavingItemComponent> codec() {
        return CODEC;
    }

    public void apply(LivingEntity target) {
        for (StatusEffectInstance effect : this.effects) {
            target.addStatusEffect(new StatusEffectInstance(effect));
        }
    }

    public static LifeSavingItemComponent of(StatusEffectInstance... effects) {
        return new LifeSavingItemComponent(List.of(effects));
    }
}
