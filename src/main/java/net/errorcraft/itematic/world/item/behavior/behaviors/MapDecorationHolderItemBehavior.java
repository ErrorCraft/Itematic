package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.MapDecorations;

public class MapDecorationHolderItemBehavior implements ItemBehavior<MapDecorationHolderItemBehavior> {
    public static final MapDecorationHolderItemBehavior INSTANCE = new MapDecorationHolderItemBehavior();
    public static final Codec<MapDecorationHolderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private MapDecorationHolderItemBehavior() {}

    @Override
    public ItemBehaviorType<MapDecorationHolderItemBehavior> type() {
        return ItemBehaviorType.MAP_DECORATION_HOLDER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
    }
}
