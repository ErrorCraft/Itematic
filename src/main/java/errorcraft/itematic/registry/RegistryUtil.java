package errorcraft.itematic.registry;

import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RegistryUtil {
    public static <T> Registry<T> getRegistry(@Nullable World world, RegistryKey<Registry<T>> key) {
        if (world == null) {
            return null;
        }
        return world.getRegistryManager().get(key);
    }

    public static <T> boolean tagContains(@Nullable World world, TagKey<T> tag, T value) {
        if (world == null) {
            return false;
        }
        return tagContains(world.getRegistryManager(), tag, value);
    }

    public static <T> boolean tagContains(DynamicRegistryManager registryManager, TagKey<T> tag, T value) {
        return tagContains(registryManager.get(tag.registry()), tag, value);
    }

    public static <T> boolean tagContains(Registry<T> registry, TagKey<T> tag, T value) {
        Optional<RegistryKey<T>> optionalRegistryKey = registry.getKey(value);
        if (optionalRegistryKey.isEmpty()) {
            return false;
        }
        return registry.entryOf(optionalRegistryKey.get()).isIn(tag);
    }
}
