package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import java.util.List;

public record SuspiciousEffectIngredientItemComponent(List<SuspiciousStewEffects.Entry> effects) implements ItemComponent<SuspiciousEffectIngredientItemComponent>, SuspiciousEffectHolder {
    public static final Codec<SuspiciousEffectIngredientItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SuspiciousStewEffects.Entry.CODEC.listOf().fieldOf("effects").forGetter(SuspiciousEffectIngredientItemComponent::effects)
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
    public SuspiciousStewEffects getSuspiciousEffects() {
        return new SuspiciousStewEffects(this.effects);
    }

    public static SuspiciousEffectIngredientItemComponent of(SuspiciousStewEffects.Entry... effects) {
        return new SuspiciousEffectIngredientItemComponent(List.of(effects));
    }
}
