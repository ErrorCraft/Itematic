package errorcraft.itematic.mixin.util.registry;

import net.minecraft.util.registry.DefaultedRegistry;
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
        method = "createIntrusive(Lnet/minecraft/util/registry/RegistryKey;Ljava/lang/String;Lnet/minecraft/util/registry/Registry$DefaultEntryGetter;)Lnet/minecraft/util/registry/DefaultedRegistry;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T> void createIntrusive(RegistryKey<? extends Registry<T>> key, String defaultId, Registry.DefaultEntryGetter<T> defaultEntryGetter, CallbackInfoReturnable<DefaultedRegistry<T>> info) {
        if (Objects.equals(key, Registry.ITEM_KEY)) {
            info.setReturnValue(null);
        }
    }
}
