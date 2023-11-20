package net.errorcraft.itematic.item.pointer.pointers;

import net.errorcraft.itematic.item.pointer.Pointer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LastDeathPointer implements Pointer {
    @Override
    public @Nullable GlobalPos createPos(ItemStack stack, World world, Entity target) {
        if (target instanceof PlayerEntity player) {
            return player.getLastDeathPos().orElse(null);
        }
        return null;
    }
}
