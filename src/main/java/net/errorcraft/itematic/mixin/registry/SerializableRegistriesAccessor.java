package net.errorcraft.itematic.mixin.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SerializableRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SerializableRegistries.class)
public interface SerializableRegistriesAccessor {
    @Invoker("getNetworkCodec")
    static <E> DataResult<? extends Codec<E>> getNetworkCodec(RegistryKey<? extends Registry<E>> registryRef) {
        throw new AssertionError();
    }

    @Invoker("createCodec")
    static Codec<DynamicRegistryManager> createCodec() {
        throw new AssertionError();
    }
}
