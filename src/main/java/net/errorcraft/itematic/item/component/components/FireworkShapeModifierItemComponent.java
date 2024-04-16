package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;

public record FireworkShapeModifierItemComponent(FireworkExplosionComponent.Type shape) implements ItemComponent<FireworkShapeModifierItemComponent> {
    public static final Codec<FireworkShapeModifierItemComponent> CODEC = FireworkExplosionComponent.Type.CODEC.xmap(FireworkShapeModifierItemComponent::new, FireworkShapeModifierItemComponent::shape);

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
