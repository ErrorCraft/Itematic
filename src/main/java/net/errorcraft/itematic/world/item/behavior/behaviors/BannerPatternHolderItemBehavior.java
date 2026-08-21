package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import java.util.Optional;

public record BannerPatternHolderItemBehavior(Optional<DyeColor> color) implements ItemBehavior<BannerPatternHolderItemBehavior> {
    public static final Codec<BannerPatternHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.optionalFieldOf("color").forGetter(BannerPatternHolderItemBehavior::color)
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
}
