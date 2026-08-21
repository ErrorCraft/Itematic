package net.errorcraft.itematic.world.item.component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface InventoryTickListener {
    void itematic$onInventoryTick(ServerLevel level, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot);
}
