package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DebugStickState;

public class DebugStickItemBehavior implements ItemBehavior {
    public static final DebugStickItemBehavior INSTANCE = new DebugStickItemBehavior();
    public static final Codec<DebugStickItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private DebugStickItemBehavior() {}

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY);
    }
}
