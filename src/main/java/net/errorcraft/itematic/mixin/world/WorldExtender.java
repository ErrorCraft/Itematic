package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldAccess;
import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(World.class)
public abstract class WorldExtender implements WorldViewAccess, WorldAccess {
    @Shadow
    public abstract void playSound(@Nullable PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch);

    @Unique
    private ItemAccess itemAccess;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void constructorSetItemAccess(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, CallbackInfo info) {
        this.itemAccess = new ItemAccess(registryManager);
    }

    @Override
    public ItemAccess itematic$getItemAccess() {
        return this.itemAccess;
    }

    @Override
    public RegistryEntry<Item> itematic$getItem(RegistryKey<Item> key) {
        return this.itemAccess.getEntry(key);
    }

    @Override
    public void itematic$playSound(@Nullable PlayerEntity except, Vec3d pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        this.playSound(except, pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch);
    }
}
