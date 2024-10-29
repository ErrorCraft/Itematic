package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BlockEntityAccess;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class BlockEntityExtender implements BlockEntityAccess {
}
