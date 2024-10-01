package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.IdentifierUtil;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemBase(String translationKey, Rarity rarity, Optional<List<Text>> tooltip, Optional<Boolean> glint) {
    public static final Codec<ItemBase> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemBase::translationKey),
        Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(ItemBase::rarity),
        TextCodecs.CODEC.listOf().optionalFieldOf("tooltip").forGetter(ItemBase::tooltip),
        Codec.BOOL.optionalFieldOf("glint").forGetter(ItemBase::glint)
    ).apply(instance, ItemBase::new));

    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.RARITY, this.rarity);
        this.glint.ifPresent(glint -> builder.add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint));
    }

    public static class Builder {
        private final String translationKey;
        private Rarity rarity = Rarity.COMMON;
        private List<Text> tooltip;
        private Boolean glint;

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
            return new ItemBase(this.translationKey, this.rarity, Optional.ofNullable(this.tooltip), Optional.ofNullable(this.glint));
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
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

        public Builder glint() {
            this.glint = true;
            return this;
        }
    }
}
