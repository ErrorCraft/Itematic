package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.item.component.FireworkExplosion;

public record FireworkShapeModifierItemBehavior(FireworkExplosion.Shape shape) implements ItemBehavior<FireworkShapeModifierItemBehavior> {
    public static final Codec<FireworkShapeModifierItemBehavior> CODEC = FireworkExplosion.Shape.CODEC.xmap(FireworkShapeModifierItemBehavior::new, FireworkShapeModifierItemBehavior::shape);

    public static FireworkShapeModifierItemBehavior of(FireworkExplosion.Shape shape) {
        return new FireworkShapeModifierItemBehavior(shape);
    }

    @Override
    public ItemBehaviorType<FireworkShapeModifierItemBehavior> type() {
        return ItemBehaviorType.FIREWORK_SHAPE_MODIFIER;
    }

}
