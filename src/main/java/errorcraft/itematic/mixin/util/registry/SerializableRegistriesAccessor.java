package errorcraft.itematic.mixin.util.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SerializableRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SerializableRegistries.class)
public interface SerializableRegistriesAccessor {
    @Invoker("add")
    static <E> void add(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder, RegistryKey<? extends Registry<E>> key, Codec<E> networkCodec) {
        throw new AssertionError();
    }
}
