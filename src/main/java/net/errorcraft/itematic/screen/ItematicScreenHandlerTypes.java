package net.errorcraft.itematic.screen;

import net.errorcraft.itematic.mixin.screen.ScreenHandlerTypeAccessor;
import net.minecraft.screen.ScreenHandlerType;

public class ItematicScreenHandlerTypes {
    public static final ScreenHandlerType<BrewingStandMenuDelegate> BREWING_STAND = ScreenHandlerTypeAccessor.register("brewing_stand", BrewingStandMenuDelegate::new);

    private ItematicScreenHandlerTypes() {}

    public static void init() {}
}
