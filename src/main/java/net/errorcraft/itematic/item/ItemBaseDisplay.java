package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.IdentifierUtil;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemBaseDisplay(String translationKey, Optional<List<Text>> tooltip) {
    public static final Codec<ItemBaseDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemBaseDisplay::translationKey),
        TextCodecs.CODEC.listOf().optionalFieldOf("tooltip").forGetter(ItemBaseDisplay::tooltip)
    ).apply(instance, ItemBaseDisplay::new));

    public static class Builder {
        private final String translationKey;
        private List<Text> tooltip;

        private Builder(String name) {
            this.translationKey = name;
        }

        public ItemBaseDisplay build() {
            return new ItemBaseDisplay(this.translationKey, Optional.ofNullable(this.tooltip));
        }

        public static Builder forItem(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("item", name.getValue()));
        }

        public static Builder forBlock(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("block", name.getValue()));
        }

        public Builder tooltip(RegistryKey<Item> name) {
            return this.tooltip(Text.translatable(IdentifierUtil.createTranslationKey(name, "item", "desc")).formatted(Formatting.GRAY));
        }

        public Builder tooltip(Text... lines) {
            if (this.tooltip == null) {
                this.tooltip = new ArrayList<>();
            }
            this.tooltip.addAll(List.of(lines));
            return this;
        }
    }
}
