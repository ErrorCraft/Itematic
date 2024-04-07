package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;

import java.util.List;

public record AttributeModifiersItemComponent(List<AttributeModifiersComponent.Entry> modifiers) implements ItemComponent<AttributeModifiersItemComponent> {
    public static final Codec<AttributeModifiersItemComponent> CODEC = AttributeModifiersComponent.Entry.CODEC.listOf().xmap(AttributeModifiersItemComponent::new, AttributeModifiersItemComponent::modifiers);

    public static AttributeModifiersItemComponent of(List<AttributeModifiersComponent.Entry> modifiers) {
        return new AttributeModifiersItemComponent(modifiers);
    }

    @Override
    public ItemComponentType<AttributeModifiersItemComponent> type() {
        return ItemComponentTypes.ATTRIBUTE_MODIFIERS;
    }

    @Override
    public Codec<AttributeModifiersItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addAttributeModifiers(AttributeModifiersComponent.Builder builder, ItemComponentSet components) {
        for (AttributeModifiersComponent.Entry modifier : this.modifiers) {
            builder.add(modifier.attribute(), modifier.modifier(), modifier.slot());
        }
    }
}
