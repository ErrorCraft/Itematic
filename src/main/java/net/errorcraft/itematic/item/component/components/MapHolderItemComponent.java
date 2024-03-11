package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapColorComponent;
import net.minecraft.component.type.MapDecorationsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record MapHolderItemComponent() implements ItemComponent<MapHolderItemComponent> {
    public static final MapHolderItemComponent INSTANCE = new MapHolderItemComponent();
    public static final Codec<MapHolderItemComponent> CODEC = Codec.unit(INSTANCE);
    private static final FilledMapItem DUMMY = new FilledMapItem(new Item.Settings());

    @Override
    public ItemComponentType<MapHolderItemComponent> type() {
        return ItemComponentTypes.MAP_HOLDER;
    }

    @Override
    public Codec<MapHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.MAP_COLOR, MapColorComponent.DEFAULT);
        builder.add(DataComponentTypes.MAP_DECORATIONS, MapDecorationsComponent.DEFAULT);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity holder, int slot, boolean selected) {
        DUMMY.inventoryTick(stack, world, holder, slot, selected);
    }

    @Override
    public void onCraft(ItemStack stack, World world) {
        DUMMY.onCraft(stack, world);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        DUMMY.appendTooltip(stack, world, tooltip, context);
    }

    public Packet<?> createSyncPacket(ItemStack stack, World world, PlayerEntity player) {
        return DUMMY.createSyncPacket(stack, world, player);
    }
}
