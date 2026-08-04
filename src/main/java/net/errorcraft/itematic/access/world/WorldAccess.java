package net.errorcraft.itematic.access.world;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface WorldAccess {
    default void itematic$playSound(@Nullable Player source, Vec3 pos, SoundEvent sound, SoundSource category, float volume, float pitch) {}
}
