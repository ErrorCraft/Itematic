package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.Optional;

public record BannerPatternHolderItemBehavior(Optional<DyeColor> bannerBaseColor) implements ItemBehavior<BannerPatternHolderItemBehavior> {
    public static final Codec<BannerPatternHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.optionalFieldOf("banner_base_color").forGetter(BannerPatternHolderItemBehavior::bannerBaseColor)
    ).apply(instance, BannerPatternHolderItemBehavior::new));

    public static BannerPatternHolderItemBehavior of() {
        return new BannerPatternHolderItemBehavior(Optional.empty());
    }

    public static BannerPatternHolderItemBehavior of(DyeColor color) {
        return new BannerPatternHolderItemBehavior(Optional.of(color));
    }

    @Override
    public ItemBehaviorType<BannerPatternHolderItemBehavior> type() {
        return ItemBehaviorType.BANNER_PATTERN_HOLDER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        this.bannerBaseColor.ifPresent(bannerBaseColor -> builder.set(ItematicDataComponents.BANNER_BASE_COLOR, bannerBaseColor));
    }
}
