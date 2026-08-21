package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapItem.class)
public interface MapItemAccessor {
    @Invoker("createNewSavedData")
    static MapId createNewSavedData(ServerLevel level, int xSpawn, int zSpawn, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
        throw new AssertionError();
    }
}
