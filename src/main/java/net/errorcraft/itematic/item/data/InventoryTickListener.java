package net.errorcraft.itematic.item.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public interface InventoryTickListener {
    void itematic$onInventoryTick(ServerWorld world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot);
}
