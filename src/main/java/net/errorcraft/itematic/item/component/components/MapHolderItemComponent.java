package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.item.component.MapItemColor;
import net.minecraft.world.level.Level;

public class MapHolderItemComponent implements ItemComponent<MapHolderItemComponent> {
    public static final MapHolderItemComponent INSTANCE = new MapHolderItemComponent();
    public static final Codec<MapHolderItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);
    public static final MapItem DUMMY = new MapItem(new Item.Properties());

    private MapHolderItemComponent() {}

    @Override
    public ItemComponentType<MapHolderItemComponent> type() {
        return ItemComponentTypes.MAP_HOLDER;
    }

    @Override
    public Codec<MapHolderItemComponent> codec() {
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
