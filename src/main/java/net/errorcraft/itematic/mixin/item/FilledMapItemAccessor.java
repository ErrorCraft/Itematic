package net.errorcraft.itematic.mixin.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapItem.class)
public interface FilledMapItemAccessor {
    @Invoker("createNewSavedData")
    static MapId allocateMapId(ServerLevel world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking, ResourceKey<Level> dimension) {
        throw new AssertionError();
    }
}
