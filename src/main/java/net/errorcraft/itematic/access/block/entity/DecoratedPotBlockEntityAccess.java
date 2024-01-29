package net.errorcraft.itematic.access.block.entity;

import net.errorcraft.itematic.block.entity.DecoratedPotBlockEntityUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Optional;

public interface DecoratedPotBlockEntityAccess {
    Optional<DecoratedPotBlockEntityUtil.Sherds> itematic$sherds();
    void itematic$readNbtFromStack(World world, ItemStack stack);
}
