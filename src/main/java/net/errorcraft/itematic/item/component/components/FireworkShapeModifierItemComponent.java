package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.StringIdentifiable;

public record FireworkShapeModifierItemComponent(FireworkRocketItem.Type shape) implements ItemComponent {
    public static final Codec<FireworkShapeModifierItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringIdentifiable.createCodec(FireworkRocketItem.Type::values).fieldOf("shape").forGetter(FireworkShapeModifierItemComponent::shape)
    ).apply(instance, FireworkShapeModifierItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FIREWORK_SHAPE_MODIFIER;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }
}
