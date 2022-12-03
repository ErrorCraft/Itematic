package errorcraft.itematic.access.network;

import net.minecraft.registry.DynamicRegistryManager;

import java.util.function.Consumer;

public interface ClientConnectionAccess {
    default void addSetRegistryManagerConsumer(Consumer<DynamicRegistryManager> consumer) {}
    default void onSetRegistryManager(DynamicRegistryManager registryManager) {}
}
