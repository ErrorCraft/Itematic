package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.world.item.component.InventoryTickListener;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LodestoneTracker.class)
public abstract class LodestoneTrackerExtender implements InventoryTickListener {
    @Shadow
    public abstract LodestoneTracker tick(ServerLevel level);

    @Override
    @SuppressWarnings("ConstantValue")
    public void itematic$onInventoryTick(ServerLevel level, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        LodestoneTracker newTracker = this.tick(level);
        if ((Object) this != newTracker) {
            stack.set(DataComponents.LODESTONE_TRACKER, newTracker);
        }
    }
}
