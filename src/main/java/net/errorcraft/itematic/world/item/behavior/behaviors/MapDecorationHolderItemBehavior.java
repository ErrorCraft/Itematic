package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.MapDecorations;

public class MapDecorationHolderItemBehavior implements ItemBehavior {
    public static final MapDecorationHolderItemBehavior INSTANCE = new MapDecorationHolderItemBehavior();
    public static final Codec<MapDecorationHolderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private MapDecorationHolderItemBehavior() {}

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
    }
}
