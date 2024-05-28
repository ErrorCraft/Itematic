package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldView.class)
public interface WorldViewExtender extends WorldViewAccess {
}
