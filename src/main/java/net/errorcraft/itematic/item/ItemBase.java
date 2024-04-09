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

public record ItemBase(String translationKey, Optional<List<Text>> tooltip) {
    public static final Codec<ItemBase> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemBase::translationKey),
        TextCodecs.CODEC.listOf().optionalFieldOf("tooltip").forGetter(ItemBase::tooltip)
    ).apply(instance, ItemBase::new));

    public static class Builder {
        private final String translationKey;
        private List<Text> tooltip;

        private Builder(String name) {
            this.translationKey = name;
        }

        public static Builder forItem(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("item", name.getValue()));
        }

        public static Builder forBlock(RegistryKey<Item> name) {
            return new Builder(Util.createTranslationKey("block", name.getValue()));
        }

        public ItemBase build() {
            return new ItemBase(this.translationKey, Optional.ofNullable(this.tooltip));
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
