package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record BannerPatternHolderItemComponent(Optional<DyeColor> color) implements ItemComponent<BannerPatternHolderItemComponent> {
    public static final Codec<BannerPatternHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.createStrictOptionalFieldCodec(DyeColor.CODEC, "color").forGetter(BannerPatternHolderItemComponent::color)
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    public boolean modifiable() {
        return this.color.isPresent();
    }

    public static BannerPatternHolderItemComponent of() {
        return new BannerPatternHolderItemComponent(Optional.empty());
    }

    public static BannerPatternHolderItemComponent of(DyeColor color) {
        return new BannerPatternHolderItemComponent(Optional.of(color));
    }
}
