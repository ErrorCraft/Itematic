package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.SuspiciousStewEffectProvider;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.List;

public record SuspiciousEffectIngredientItemBehavior(SuspiciousStewEffectProvider provider) implements ItemBehavior<SuspiciousEffectIngredientItemBehavior> {
    public static final Codec<SuspiciousEffectIngredientItemBehavior> CODEC = SuspiciousStewEffectProvider.CODEC.xmap(
        SuspiciousEffectIngredientItemBehavior::new,
        SuspiciousEffectIngredientItemBehavior::provider
    );

    public static SuspiciousEffectIngredientItemBehavior of(SuspiciousStewEffects.Entry... effects) {
        return new SuspiciousEffectIngredientItemBehavior(
            new SuspiciousStewEffectProvider(
                new SuspiciousStewEffects(
                    List.of(effects)
                )
            )
        );
    }

    @Override
    public ItemBehaviorType<SuspiciousEffectIngredientItemBehavior> type() {
        return ItemBehaviorType.SUSPICIOUS_EFFECT_INGREDIENT;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.PROVIDES_SUSPICIOUS_STEW_EFFECTS, this.provider);
    }
}
