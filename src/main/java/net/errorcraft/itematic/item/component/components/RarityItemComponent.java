package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.Rarity;

public record RarityItemComponent(Rarity rarity) implements ItemComponent<RarityItemComponent> {
    public static final Codec<RarityItemComponent> CODEC = Rarity.CODEC.xmap(RarityItemComponent::new, RarityItemComponent::rarity);

    @Override
    public ItemComponentType<RarityItemComponent> type() {
        return ItemComponentTypes.RARITY;
    }

    @Override
    public Codec<RarityItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.RARITY, this.rarity);
    }

    public static RarityItemComponent of(Rarity rarity) {
        return new RarityItemComponent(rarity);
    }
}
