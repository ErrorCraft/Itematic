package errorcraft.itematic.mixin.util.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BuiltinRegistries.class)
public interface BuiltinRegistriesAccessor {
    @Invoker("addRegistry")
    static <T, R extends MutableRegistry<T>> R addRegistry(RegistryKey<? extends Registry<T>> registryRef, R registry, BuiltinRegistries.Initializer<T> initializer, Lifecycle lifecycle) {
        throw new AssertionError();
    }
}
