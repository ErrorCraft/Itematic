package net.errorcraft.itematic.world.inventory;

import net.errorcraft.itematic.mixin.world.inventory.MenuTypeAccessor;
import net.minecraft.world.inventory.MenuType;

public class ItematicMenuTypes {
    public static final MenuType<BrewingStandMenuDelegate> BREWING_STAND = MenuTypeAccessor.register("brewing_stand", BrewingStandMenuDelegate::new);

    private ItematicMenuTypes() {}

    public static void init() {}
}
