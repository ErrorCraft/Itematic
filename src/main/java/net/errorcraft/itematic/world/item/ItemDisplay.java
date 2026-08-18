package net.errorcraft.itematic.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.references.ItemBarStyleIds;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemDisplay(String translationKey, Identifier model, Rarity rarity, Optional<List<Component>> tooltip, Optional<Boolean> glint, Identifier itemBarStyle, Optional<Identifier> tooltipStyle) {
    public static final Codec<ItemDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("translation_key").forGetter(ItemDisplay::translationKey),
        Identifier.CODEC.fieldOf("model").forGetter(ItemDisplay::model),
        Rarity.CODEC.optionalFieldOf("rarity", Rarity.COMMON).forGetter(ItemDisplay::rarity),
        ComponentSerialization.CODEC.listOf().optionalFieldOf("tooltip").forGetter(ItemDisplay::tooltip),
        Codec.BOOL.optionalFieldOf("glint").forGetter(ItemDisplay::glint),
        Identifier.CODEC.optionalFieldOf("item_bar_style", ItemBarStyleIds.DAMAGE).forGetter(ItemDisplay::itemBarStyle),
        Identifier.CODEC.optionalFieldOf("tooltip_style").forGetter(ItemDisplay::tooltipStyle)
    ).apply(instance, ItemDisplay::new));

    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.ITEM_NAME, Component.translatable(this.translationKey));
        builder.set(DataComponents.ITEM_MODEL, this.model);
        builder.set(DataComponents.RARITY, this.rarity);
        this.glint.ifPresent(glint -> builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, glint));
        builder.set(ItematicDataComponents.ITEM_BAR_STYLE, this.itemBarStyle);
        this.tooltipStyle.ifPresent(tooltipStyle -> builder.set(DataComponents.TOOLTIP_STYLE, tooltipStyle));
    }

    public static class Builder {
        private static final DependantName<Item, String> ITEM_NAME_SUPPLIER = ItemAccessor.PropertiesAccessor.itemNameSupplier();
        private static final DependantName<Item, String> BLOCK_NAME_SUPPLIER = ItemAccessor.PropertiesAccessor.blockNameSupplier();

        private final String translationKey;
        private final Identifier model;
        private Rarity rarity = Rarity.COMMON;
        @Nullable
        private List<Component> tooltip;
        @Nullable
        private Boolean glint;
        private Identifier itemBarStyle = ItemBarStyleIds.DAMAGE;

        private Builder(ResourceKey<Item> name, DependantName<Item, String> nameSupplier) {
            this.translationKey = nameSupplier.get(name);
            this.model = name.identifier();
        }

        public static Builder forItem(ResourceKey<Item> name) {
            return new Builder(name, ITEM_NAME_SUPPLIER);
        }

        public static Builder forBlock(ResourceKey<Item> name) {
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

        public Builder tooltip(Component... lines) {
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
