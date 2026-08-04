package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelReader.class)
public interface WorldViewExtender extends WorldViewAccess {
}
