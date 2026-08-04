package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

public record DecoratedPotPatternItemComponent(Holder<DecoratedPotPattern> pattern) implements ItemComponent<DecoratedPotPatternItemComponent> {
    public static final Codec<DecoratedPotPatternItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.DECORATED_POT_PATTERN).fieldOf("pattern").forGetter(DecoratedPotPatternItemComponent::pattern)
    ).apply(instance, DecoratedPotPatternItemComponent::new));

    public static ItemComponent<?>[] of(Holder<DecoratedPotPattern> pattern) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(64),
            new DecoratedPotPatternItemComponent(pattern)
        };
    }

    @Override
    public ItemComponentType<DecoratedPotPatternItemComponent> type() {
        return ItemComponentTypes.DECORATED_POT_PATTERN;
    }

    @Override
    public Codec<DecoratedPotPatternItemComponent> codec() {
        return CODEC;
    }
}
