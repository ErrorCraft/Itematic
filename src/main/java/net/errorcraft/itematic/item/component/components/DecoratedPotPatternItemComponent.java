package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record DecoratedPotPatternItemComponent(RegistryEntry<DecoratedPotPattern> pattern) implements ItemComponent<DecoratedPotPatternItemComponent> {
    public static final Codec<DecoratedPotPatternItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.DECORATED_POT_PATTERN).fieldOf("pattern").forGetter(DecoratedPotPatternItemComponent::pattern)
    ).apply(instance, DecoratedPotPatternItemComponent::new));

    public static ItemComponent<?>[] of(RegistryEntry<DecoratedPotPattern> pattern) {
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
