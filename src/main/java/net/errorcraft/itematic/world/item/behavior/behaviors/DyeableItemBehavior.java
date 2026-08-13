package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;

public class DyeableItemBehavior implements ItemBehavior<DyeableItemBehavior> {
    public static final DyeableItemBehavior INSTANCE = new DyeableItemBehavior();
    public static final Codec<DyeableItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private DyeableItemBehavior() {}

    @Override
    public ItemBehaviorType<DyeableItemBehavior> type() {
        return ItemBehaviorType.DYEABLE;
    }

    @Override
    public Codec<DyeableItemBehavior> codec() {
        return CODEC;
    }
}
