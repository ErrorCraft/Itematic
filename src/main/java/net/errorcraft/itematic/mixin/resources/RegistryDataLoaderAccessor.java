package net.errorcraft.itematic.mixin.resources;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RegistryDataLoaderAccessor {
    @Mixin(RegistryDataLoader.RegistryData.class)
    interface RegistryDataAccessor {
        @Invoker("<init>")
        static <T> RegistryDataLoader.RegistryData<T> create(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) {
            throw new AssertionError();
        }
    }
}
