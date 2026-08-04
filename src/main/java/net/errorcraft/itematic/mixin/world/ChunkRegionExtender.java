package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.WorldViewAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldGenRegion.class)
public class ChunkRegionExtender implements WorldViewAccess {
    @Shadow
    @Final
    private ServerLevel level;

    @Override
    public ItemAccess itematic$getItemAccess() {
        return this.level.itematic$getItemAccess();
    }

    @Override
    public Holder<Item> itematic$getItem(ResourceKey<Item> key) {
        return this.level.itematic$getItem(key);
    }
}
