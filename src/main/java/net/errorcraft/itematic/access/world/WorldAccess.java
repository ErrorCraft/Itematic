package net.errorcraft.itematic.access.world;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface WorldAccess {
    default void playSound(@Nullable PlayerEntity except, Vec3d pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {}
}
