package net.errorcraft.itematic.mixin.world.level;

import net.errorcraft.itematic.access.world.level.LevelAccess;
import net.errorcraft.itematic.access.world.level.LevelReaderAccess;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelExtender implements LevelReaderAccess, LevelAccess {
    @Shadow
    public abstract void playSound(@Nullable Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch);

    @Unique
    private ItemAccess itemAccess;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void constructorSetItemAccess(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates, CallbackInfo info) {
        this.itemAccess = new ItemAccess(registryAccess);
    }

    @Override
    public ItemAccess itematic$itemAccess() {
        return this.itemAccess;
    }

    @Override
    public void itematic$playSound(@Nullable Player source, Vec3 pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
        this.playSound(source, pos.x(), pos.y(), pos.z(), sound, category, volume, pitch);
    }
}
