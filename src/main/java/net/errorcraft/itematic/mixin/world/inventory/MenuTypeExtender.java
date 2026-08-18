package net.errorcraft.itematic.mixin.world.inventory;

import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.errorcraft.itematic.world.inventory.ItematicMenuTypes;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MenuType.class)
public class MenuTypeExtender {
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
    private static MenuType<BrewingStandMenuDelegate> useDelegatingBrewingStandMenu(String id, MenuType.MenuSupplier<BrewingStandMenu> factory) {
        return ItematicMenuTypes.BREWING_STAND;
    }
}
