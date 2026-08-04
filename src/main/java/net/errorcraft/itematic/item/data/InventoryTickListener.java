package net.errorcraft.itematic.item.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface InventoryTickListener {
    void itematic$onInventoryTick(ServerLevel world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot);
}
