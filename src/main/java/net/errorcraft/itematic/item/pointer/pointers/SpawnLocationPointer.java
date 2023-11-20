package net.errorcraft.itematic.item.pointer.pointers;

import net.errorcraft.itematic.item.pointer.Pointer;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpawnLocationPointer implements Pointer {
    @Override
    public @Nullable GlobalPos createPos(ItemStack stack, World world, Entity target) {
        if (CompassItem.hasLodestone(stack)) {
            return CompassItem.createLodestonePos(stack.getOrCreateNbt());
        }
        return CompassItem.createSpawnPos(world);
    }
}
