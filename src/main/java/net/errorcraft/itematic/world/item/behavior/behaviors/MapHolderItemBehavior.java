package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.item.component.MapItemColor;
import net.minecraft.world.level.Level;

public class MapHolderItemBehavior implements ItemBehavior<MapHolderItemBehavior> {
    public static final MapHolderItemBehavior INSTANCE = new MapHolderItemBehavior();
    public static final Codec<MapHolderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);
    public static final MapItem DUMMY = new MapItem(new Item.Properties());

    private MapHolderItemBehavior() {}

    @Override
    public ItemBehaviorType<MapHolderItemBehavior> type() {
        return ItemBehaviorType.MAP_HOLDER;
    }

    @Override
    public Codec<MapHolderItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAP_COLOR, MapItemColor.DEFAULT);
        builder.set(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY);
    }

    @Override
    public void onCraft(ItemStack stack, Level world) {
        DUMMY.onCraftedPostProcess(stack, world);
    }
}
