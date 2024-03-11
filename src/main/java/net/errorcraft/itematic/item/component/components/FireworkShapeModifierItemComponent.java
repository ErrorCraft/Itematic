package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.util.StringIdentifiable;

public record FireworkShapeModifierItemComponent(FireworkExplosionComponent.Type shape) implements ItemComponent<FireworkShapeModifierItemComponent> {
    public static final Codec<FireworkShapeModifierItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringIdentifiable.createCodec(FireworkExplosionComponent.Type::values).fieldOf("shape").forGetter(FireworkShapeModifierItemComponent::shape)
    ).apply(instance, FireworkShapeModifierItemComponent::new));

    @Override
    public ItemComponentType<FireworkShapeModifierItemComponent> type() {
        return ItemComponentTypes.FIREWORK_SHAPE_MODIFIER;
    }

    @Override
    public Codec<FireworkShapeModifierItemComponent> codec() {
        return CODEC;
    }

    public static FireworkShapeModifierItemComponent of(FireworkExplosionComponent.Type shape) {
        return new FireworkShapeModifierItemComponent(shape);
    }
}
