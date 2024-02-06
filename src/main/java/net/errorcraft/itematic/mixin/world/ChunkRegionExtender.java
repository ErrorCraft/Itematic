package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ChunkRegion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
public class ChunkRegionExtender implements WorldViewAccess {
    @Shadow
    @Final
    private ServerWorld world;

    @Override
    public ItemAccess itematic$getItemAccess() {
        return this.world.itematic$getItemAccess();
    }

    @Override
    public RegistryEntry<Item> itematic$getItem(RegistryKey<Item> key) {
        return this.world.itematic$getItem(key);
    }
}
