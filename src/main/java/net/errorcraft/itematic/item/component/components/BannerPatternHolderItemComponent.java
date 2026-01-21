package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.List;
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
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    public boolean modifiable() {
        return this.color.isPresent();
    }

    public Optional<String> translationKey(ItemStack stack, String baseTranslationKey) {
        if (this.modifiable()) {
            return Optional.empty();
        }
        DyeColor baseColor = stack.get(DataComponentTypes.BASE_COLOR);
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
