package errorcraft.itematic.mixin.util.registry;

import errorcraft.itematic.item.ItemUtil;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesExtender {
    @Shadow
    @Final
    private static RegistryBuilder REGISTRY_BUILDER;

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void initialiseItemRegistry(CallbackInfo info) {
        REGISTRY_BUILDER.addRegistry(Registry.ITEM_KEY, ItemUtil::initAndGetDefault);
    }
}
