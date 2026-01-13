package net.errorcraft.itematic.mixin.registry;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RegistryLoaderAccessor {
    @Mixin(RegistryLoader.Entry.class)
    interface EntryAccessor {
        @Invoker("<init>")
        static <T> RegistryLoader.Entry<T> create(RegistryKey<? extends Registry<T>> registryKey, Codec<T> codec) {
            throw new AssertionError();
        }
    }
}
