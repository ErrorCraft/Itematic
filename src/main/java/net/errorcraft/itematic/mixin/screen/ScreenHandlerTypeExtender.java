package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.screen.ItematicScreenHandlerTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MenuType.class)
public class ScreenHandlerTypeExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/MenuType;register(Ljava/lang/String;Lnet/minecraft/world/inventory/MenuType$MenuSupplier;)Lnet/minecraft/world/inventory/MenuType;",
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
    private static <T extends AbstractContainerMenu> MenuType<T> useCustomGliderDataComponent(String id, MenuType.MenuSupplier<T> factory) {
        return (MenuType<T>) ItematicScreenHandlerTypes.BREWING_STAND;
    }
}
