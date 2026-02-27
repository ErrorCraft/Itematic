package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ScreenHandlerType.class)
public class ScreenHandlerTypeExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/ScreenHandlerType;register(Ljava/lang/String;Lnet/minecraft/screen/ScreenHandlerType$Factory;)Lnet/minecraft/screen/ScreenHandlerType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=brewing_stand"
            )
        )
    )
    @SuppressWarnings("unchecked")
    private static <T extends ScreenHandler> ScreenHandlerType<T> useCustomGliderDataComponent(String id, ScreenHandlerType.Factory<T> factory) {
        return (ScreenHandlerType<T>) ItematicScreenHandlerTypes.BREWING_STAND;
    }
}
