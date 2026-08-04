package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.item.component.FireworkExplosion;

public record FireworkShapeModifierItemComponent(FireworkExplosion.Shape shape) implements ItemComponent<FireworkShapeModifierItemComponent> {
    public static final Codec<FireworkShapeModifierItemComponent> CODEC = FireworkExplosion.Shape.CODEC.xmap(FireworkShapeModifierItemComponent::new, FireworkShapeModifierItemComponent::shape);

    @Override
    public ItemComponentType<FireworkShapeModifierItemComponent> type() {
        return ItemComponentTypes.FIREWORK_SHAPE_MODIFIER;
    }

    @Override
    public Codec<FireworkShapeModifierItemComponent> codec() {
        return CODEC;
    }

    public static FireworkShapeModifierItemComponent of(FireworkExplosion.Shape shape) {
        return new FireworkShapeModifierItemComponent(shape);
    }
}
