package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.data.InventoryTickListener;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LodestoneTracker.class)
public abstract class LodestoneTrackerComponentExtender implements InventoryTickListener {
    @Shadow
    public abstract LodestoneTracker tick(ServerLevel world);

    @Override
    @SuppressWarnings("ConstantValue")
    public void itematic$onInventoryTick(ServerLevel world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        LodestoneTracker newTracker = this.tick(world);
        if ((Object) this != newTracker) {
            stack.set(DataComponents.LODESTONE_TRACKER, newTracker);
        }
    }
}
