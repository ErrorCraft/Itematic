package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public record BannerPatternItemComponent(TagKey<BannerPattern> patterns) implements ItemComponent<BannerPatternItemComponent> {
    public static final Codec<BannerPatternItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.codec(Registries.BANNER_PATTERN).fieldOf("patterns").forGetter(BannerPatternItemComponent::patterns)
    ).apply(instance, BannerPatternItemComponent::new));

    public static ItemComponent<?>[] of(TagKey<BannerPattern> patterns) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            new BannerPatternItemComponent(patterns)
        };
    }

    @Override
    public ItemComponentType<BannerPatternItemComponent> type() {
        return ItemComponentTypes.BANNER_PATTERN;
    }

    @Override
    public Codec<BannerPatternItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.PROVIDES_BANNER_PATTERNS, this.patterns);
    }
}
