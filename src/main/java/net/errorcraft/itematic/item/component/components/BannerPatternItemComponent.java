package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record BannerPatternItemComponent(TagKey<BannerPattern> patterns) implements ItemComponent<BannerPatternItemComponent> {
    public static final Codec<BannerPatternItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.unprefixedCodec(RegistryKeys.BANNER_PATTERN).fieldOf("patterns").forGetter(BannerPatternItemComponent::patterns)
    ).apply(instance, BannerPatternItemComponent::new));

    @Override
    public ItemComponentType<BannerPatternItemComponent> type() {
        return ItemComponentTypes.BANNER_PATTERN;
    }

    @Override
    public Codec<BannerPatternItemComponent> codec() {
        return CODEC;
    }

    public static ItemComponent<?>[] of(TagKey<BannerPattern> patterns) {
        return new ItemComponent<?>[] {
            MaxStackSizeItemComponent.of(1),
            new BannerPatternItemComponent(patterns)
        };
    }
}
