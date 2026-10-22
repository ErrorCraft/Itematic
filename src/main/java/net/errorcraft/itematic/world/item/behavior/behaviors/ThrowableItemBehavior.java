package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.Throwable;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.component.DataComponentMap;

import java.util.Optional;

public record ThrowableItemBehavior(Throwable throwable) implements ItemBehavior<ThrowableItemBehavior> {
    public static final Codec<ThrowableItemBehavior> CODEC = Throwable.CODEC.xmap(
        ThrowableItemBehavior::new,
        ThrowableItemBehavior::throwable
    );

    public static ThrowableItemBehavior of() {
        return new ThrowableItemBehavior(new Throwable(0.0f, 0.0f, Optional.empty()));
    }

    public static ThrowableItemBehavior of(float speed) {
        return new ThrowableItemBehavior(new Throwable(speed, 0.0f, Optional.empty()));
    }

    public static ThrowableItemBehavior of(float speed, float angleOffset) {
        return new ThrowableItemBehavior(new Throwable(speed, angleOffset, Optional.empty()));
    }

    public static ThrowableItemBehavior of(float speed, float angleOffset, int minDrawDuration) {
        return new ThrowableItemBehavior(
            new Throwable(speed,
                angleOffset,
                Optional.of(MinMaxBounds.Ints.atLeast(minDrawDuration))
            )
        );
    }

    @Override
    public ItemBehaviorType<ThrowableItemBehavior> type() {
        return ItemBehaviorType.THROWABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.THROWABLE, this.throwable);
    }
}
