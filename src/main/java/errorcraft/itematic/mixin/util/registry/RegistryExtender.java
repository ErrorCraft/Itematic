package errorcraft.itematic.mixin.util.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Registry.class)
public class RegistryExtender {
    @Inject(
        method = "create(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/MutableRegistry;Lnet/minecraft/util/registry/Registry$DefaultEntryGetter;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/util/registry/MutableRegistry;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T, R extends MutableRegistry<T>> void doNotAddItemRegistry(RegistryKey<? extends Registry<T>> key, R registry, Registry.DefaultEntryGetter<T> defaultEntryGetter, Lifecycle lifecycle, CallbackInfoReturnable<R> info) {
        if (Objects.equals(key, Registry.ITEM_KEY)) {
            info.setReturnValue(registry);
        }
    }
}
