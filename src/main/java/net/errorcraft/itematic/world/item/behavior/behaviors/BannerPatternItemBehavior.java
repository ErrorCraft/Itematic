package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;

public record BannerPatternItemBehavior(HolderSet<BannerPattern> patterns) implements ItemBehavior<BannerPatternItemBehavior> {
    public static final Codec<BannerPatternItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.BANNER_PATTERN).fieldOf("patterns").forGetter(BannerPatternItemBehavior::patterns)
    ).apply(instance, BannerPatternItemBehavior::new));

    public static ItemBehavior<?>[] of(HolderSet<BannerPattern> patterns) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            new BannerPatternItemBehavior(patterns)
        };
    }

    @Override
    public ItemBehaviorType<BannerPatternItemBehavior> type() {
        return ItemBehaviorType.BANNER_PATTERN;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.PROVIDES_BANNER_PATTERNS, this.patterns);
    }
}
