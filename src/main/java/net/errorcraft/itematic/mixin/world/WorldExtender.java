package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldAccess;
import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class WorldExtender implements WorldViewAccess, WorldAccess {
    @Shadow
    public abstract void playSound(@Nullable Entity source, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch);

    @Unique
    private ItemAccess itemAccess;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void constructorSetItemAccess(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates, CallbackInfo info) {
        this.itemAccess = new ItemAccess(registryManager);
    }

    @Override
    public ItemAccess itematic$getItemAccess() {
        return this.itemAccess;
    }

    @Override
    public Holder<Item> itematic$getItem(ResourceKey<Item> key) {
        return this.itemAccess.getEntry(key);
    }

    @Override
    public void itematic$playSound(@Nullable Player source, Vec3 pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
        this.playSound(source, pos.x(), pos.y(), pos.z(), sound, category, volume, pitch);
    }
}
