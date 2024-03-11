package net.errorcraft.itematic.mixin.item;

import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.FilledMapItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FilledMapItem.class)
public interface FilledMapItemAccessor {
    @Invoker("allocateMapId")
    static MapIdComponent allocateMapId(World world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking, RegistryKey<World> dimension) {
        throw new AssertionError();
    }
}
