package net.errorcraft.itematic.screen;

import net.errorcraft.itematic.mixin.world.inventory.MenuTypeAccessor;
import net.minecraft.world.inventory.MenuType;

public class ItematicScreenHandlerTypes {
    public static final MenuType<BrewingStandMenuDelegate> BREWING_STAND = MenuTypeAccessor.register("brewing_stand", BrewingStandMenuDelegate::new);

    private ItematicScreenHandlerTypes() {}

    public static void init() {}
}
