package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import java.util.Optional;

public record BannerPatternHolderItemComponent(Optional<DyeColor> color) implements ItemComponent<BannerPatternHolderItemComponent> {
    public static final Codec<BannerPatternHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.optionalFieldOf("color").forGetter(BannerPatternHolderItemComponent::color)
    ).apply(instance, BannerPatternHolderItemComponent::new));

    @Override
    public ItemComponentType<BannerPatternHolderItemComponent> type() {
        return ItemComponentTypes.BANNER_PATTERN_HOLDER;
    }

    @Override
    public Codec<BannerPatternHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
    }

    public boolean modifiable() {
        return this.color.isPresent();
    }

    public Optional<String> translationKey(ItemStack stack, String baseTranslationKey) {
        if (this.modifiable()) {
            return Optional.empty();
        }

        DyeColor baseColor = stack.get(DataComponents.BASE_COLOR);
        if (baseColor == null) {
            return Optional.empty();
        }

        return Optional.of(baseTranslationKey + "." + baseColor.getName());
    }

    public static BannerPatternHolderItemComponent of() {
        return new BannerPatternHolderItemComponent(Optional.empty());
    }

    public static BannerPatternHolderItemComponent of(DyeColor color) {
        return new BannerPatternHolderItemComponent(Optional.of(color));
    }
}
