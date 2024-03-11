package net.errorcraft.itematic.item.pointer.pointers;

import net.errorcraft.itematic.item.pointer.Pointer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpawnLocationPointer implements Pointer {
    @Override
    public @Nullable GlobalPos createPos(ItemStack stack, World world, Entity target) {
        LodestoneTrackerComponent lodestoneTracker = stack.get(DataComponentTypes.LODESTONE_TRACKER);
        if (lodestoneTracker != null) {
            return lodestoneTracker.target().orElse(null);
        }
        return CompassItem.createSpawnPos(world);
    }
}
