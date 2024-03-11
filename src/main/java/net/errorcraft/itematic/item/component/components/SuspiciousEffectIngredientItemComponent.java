package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;

import java.util.List;

public record SuspiciousEffectIngredientItemComponent(List<SuspiciousStewEffectsComponent.StewEffect> effects) implements ItemComponent<SuspiciousEffectIngredientItemComponent>, SuspiciousStewIngredient {
    public static final Codec<SuspiciousEffectIngredientItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SuspiciousStewEffectsComponent.StewEffect.CODEC.listOf().fieldOf("effects").forGetter(SuspiciousEffectIngredientItemComponent::effects)
    ).apply(instance, SuspiciousEffectIngredientItemComponent::new));

    @Override
    public ItemComponentType<SuspiciousEffectIngredientItemComponent> type() {
        return ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT;
    }

    @Override
    public Codec<SuspiciousEffectIngredientItemComponent> codec() {
        return CODEC;
    }

    @Override
    public SuspiciousStewEffectsComponent getStewEffects() {
        return new SuspiciousStewEffectsComponent(this.effects);
    }

    public static SuspiciousEffectIngredientItemComponent of(SuspiciousStewEffectsComponent.StewEffect... effects) {
        return new SuspiciousEffectIngredientItemComponent(List.of(effects));
    }
}
