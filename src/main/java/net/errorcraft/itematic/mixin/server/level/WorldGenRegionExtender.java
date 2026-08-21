package net.errorcraft.itematic.mixin.server.level;

import net.errorcraft.itematic.access.world.level.LevelReaderAccess;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionExtender implements LevelReaderAccess {
    @Shadow
    @Final
    private ServerLevel level;

    @Override
    public ItemAccess itematic$itemAccess() {
        return this.level.itematic$itemAccess();
    }
}
