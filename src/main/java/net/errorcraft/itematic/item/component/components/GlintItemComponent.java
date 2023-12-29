package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;

public record GlintItemComponent(boolean glint) implements ItemComponent {
    public static final Codec<GlintItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("glint").forGetter(GlintItemComponent::glint)
    ).apply(instance, GlintItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.GLINT;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    public static GlintItemComponent of(boolean glint) {
        return new GlintItemComponent(glint);
    }
}
