package errorcraft.itematic.mixin.util.registry;

import errorcraft.itematic.item.ItemUtil;
import net.minecraft.util.registry.BuiltinRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesExtender {
    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/BuiltinRegistries;addRegistry(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/BuiltinRegistries$Initializer;)Lnet/minecraft/util/registry/Registry;", ordinal = 0, shift = At.Shift.BEFORE)
    )
    private static void initialiseItemRegistry(CallbackInfo info) {
        ItemUtil.init();
    }
}
