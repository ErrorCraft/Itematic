package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.Castable;
import net.minecraft.core.component.DataComponentMap;

public record CastableItemBehavior(Castable castable) implements ItemBehavior<CastableItemBehavior> {
    public static final Codec<CastableItemBehavior> CODEC = Castable.CODEC.xmap(
        CastableItemBehavior::new,
        CastableItemBehavior::castable
    );

    public static CastableItemBehavior of(Castable castable) {
        return new CastableItemBehavior(castable);
    }

    @Override
    public ItemBehaviorType<CastableItemBehavior> type() {
        return ItemBehaviorType.CASTABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.CASTABLE, this.castable);
    }
}
