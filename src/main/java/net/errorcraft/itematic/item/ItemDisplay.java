package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.util.IdentifierUtil;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeyedValue;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemDisplay(String translationKey, Identifier model, Rarity rarity, Optional<List<Text>> tooltip, Optional<Boolean> glint, Identifier itemBarStyle, Optional<Identifier> tooltipStyle) {
    public static final Codec<ItemDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemDisplay::translationKey),
        Identifier.CODEC.fieldOf("model").forGetter(ItemDisplay::model),
        Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(ItemDisplay::rarity),
        TextCodecs.CODEC.listOf().optionalFieldOf("tooltip").forGetter(ItemDisplay::tooltip),
        Codec.BOOL.optionalFieldOf("glint").forGetter(ItemDisplay::glint),
        Identifier.CODEC.optionalFieldOf("item_bar_style", ItemBarStyleKeys.DAMAGE).forGetter(ItemDisplay::itemBarStyle),
        Identifier.CODEC.optionalFieldOf("tooltip_style").forGetter(ItemDisplay::tooltipStyle)
    ).apply(instance, ItemDisplay::new));

    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.ITEM_NAME, Text.translatable(this.translationKey));
        builder.add(DataComponentTypes.ITEM_MODEL, this.model);
        builder.add(DataComponentTypes.RARITY, this.rarity);
        this.glint.ifPresent(glint -> builder.add(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint));
        builder.add(ItematicDataComponentTypes.ITEM_BAR_STYLE, this.itemBarStyle);
        this.tooltipStyle.ifPresent(tooltipStyle -> builder.add(DataComponentTypes.TOOLTIP_STYLE, tooltipStyle));
    }

    public static class Builder {
        private static final RegistryKeyedValue<Item, String> ITEM_NAME_SUPPLIER = ItemAccessor.SettingsAccessor.itemNameSupplier();
        private static final RegistryKeyedValue<Item, String> BLOCK_NAME_SUPPLIER = ItemAccessor.SettingsAccessor.blockNameSupplier();

        private final String translationKey;
        private final Identifier model;
        private Rarity rarity = Rarity.COMMON;
        private List<Text> tooltip;
        private Boolean glint;
        private Identifier itemBarStyle = ItemBarStyleKeys.DAMAGE;

        private Builder(RegistryKey<Item> name, RegistryKeyedValue<Item, String> nameSupplier) {
            this.translationKey = nameSupplier.get(name);
            this.model = name.getValue();
        }

        public static Builder forItem(RegistryKey<Item> name) {
            return new Builder(name, ITEM_NAME_SUPPLIER);
        }

        public static Builder forBlock(RegistryKey<Item> name) {
            return new Builder(name, BLOCK_NAME_SUPPLIER);
        }

        public ItemDisplay build() {
            return new ItemDisplay(
                this.translationKey,
                this.model,
                this.rarity,
                Optional.ofNullable(this.tooltip),
                Optional.ofNullable(this.glint),
                this.itemBarStyle,
                Optional.empty()
            );
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

        public Builder itemBarStyle(Identifier itemBarStyle) {
            this.itemBarStyle = itemBarStyle;
            return this;
        }
    }
}
