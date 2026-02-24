package net.errorcraft.itematic.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.type.AttributeModifiersComponent;

public class AttributeModifiersComponentUtil {
    public static final Codec<AttributeModifiersComponent> LIST_CODEC = AttributeModifiersComponent.Entry.CODEC.listOf().xmap(
        entries -> new AttributeModifiersComponent(entries, true),
        AttributeModifiersComponent::modifiers
    );

    private AttributeModifiersComponentUtil() {}
}
