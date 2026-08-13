package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import java.util.List;

public record SuspiciousEffectIngredientItemBehavior(List<SuspiciousStewEffects.Entry> effects) implements ItemBehavior<SuspiciousEffectIngredientItemBehavior>, SuspiciousEffectHolder {
    public static final Codec<SuspiciousEffectIngredientItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SuspiciousStewEffects.Entry.CODEC.listOf().fieldOf("effects").forGetter(SuspiciousEffectIngredientItemBehavior::effects)
    ).apply(instance, SuspiciousEffectIngredientItemBehavior::new));

    public static SuspiciousEffectIngredientItemBehavior of(SuspiciousStewEffects.Entry... effects) {
        return new SuspiciousEffectIngredientItemBehavior(List.of(effects));
    }

    @Override
    public ItemBehaviorType<SuspiciousEffectIngredientItemBehavior> type() {
        return ItemBehaviorType.SUSPICIOUS_EFFECT_INGREDIENT;
    }

    @Override
    public Codec<SuspiciousEffectIngredientItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return new SuspiciousStewEffects(this.effects);
    }
}
