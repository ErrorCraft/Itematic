package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DebugStickState;

public class DebugStickItemBehavior implements ItemBehavior<DebugStickItemBehavior> {
    public static final DebugStickItemBehavior INSTANCE = new DebugStickItemBehavior();
    public static final Codec<DebugStickItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private DebugStickItemBehavior() {}

    @Override
    public ItemBehaviorType<DebugStickItemBehavior> type() {
        return ItemBehaviorType.DEBUG_STICK;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY);
    }
}
