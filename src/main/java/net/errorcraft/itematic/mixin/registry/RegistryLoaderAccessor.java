package net.errorcraft.itematic.mixin.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RegistryLoaderAccessor {
    @Mixin(RegistryDataLoader.RegistryData.class)
    interface EntryAccessor {
        @Invoker("<init>")
        static <T> RegistryDataLoader.RegistryData<T> create(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
            throw new AssertionError();
        }
    }
}
