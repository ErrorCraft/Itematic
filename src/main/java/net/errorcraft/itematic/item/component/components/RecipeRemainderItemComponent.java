package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record RecipeRemainderItemComponent(RegistryEntry<Item> item) implements ItemComponent<RecipeRemainderItemComponent> {
    public static final Codec<RecipeRemainderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(RecipeRemainderItemComponent::item)
    ).apply(instance, RecipeRemainderItemComponent::new));

    @Override
    public ItemComponentType<RecipeRemainderItemComponent> type() {
        return ItemComponentTypes.RECIPE_REMAINDER;
    }

    @Override
    public Codec<RecipeRemainderItemComponent> codec() {
        return CODEC;
    }

    public static RecipeRemainderItemComponent of(RegistryEntry<Item> item) {
        return new RecipeRemainderItemComponent(item);
    }
}
