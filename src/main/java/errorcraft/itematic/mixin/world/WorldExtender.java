package errorcraft.itematic.mixin.world;

import errorcraft.itematic.access.world.WorldAccess;
import errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(World.class)
public class WorldExtender implements WorldAccess {
    private ItemAccess itemAccess;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void constructorSetItemAccess(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, CallbackInfo info) {
        this.itemAccess = new ItemAccess(registryManager);
    }

    @Override
    public ItemAccess getItemAccess() {
        return this.itemAccess;
    }

    @Override
    public RegistryEntry<Item> getItem(RegistryKey<Item> key) {
        return this.itemAccess.getEntry(key);
    }
}
