package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.data.InventoryTickListener;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LodestoneTrackerComponent.class)
public abstract class LodestoneTrackerComponentExtender implements InventoryTickListener {
    @Shadow
    public abstract LodestoneTrackerComponent forWorld(ServerWorld world);

    @Override
    @SuppressWarnings("ConstantValue")
    public void itematic$onInventoryTick(ServerWorld world, ItemStack stack, Entity owner, @Nullable EquipmentSlot slot) {
        LodestoneTrackerComponent newTracker = this.forWorld(world);
        if ((Object) this != newTracker) {
            stack.set(DataComponentTypes.LODESTONE_TRACKER, newTracker);
        }
    }
}
