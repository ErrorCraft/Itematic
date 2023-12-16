package net.errorcraft.itematic.loot.context;

import net.errorcraft.itematic.mixin.loot.context.LootContextParametersAccessor;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.util.math.Direction;

public class ItematicLootContextParameters {
    public static final LootContextParameter<Direction> SIDE = LootContextParametersAccessor.register("side");

    private ItematicLootContextParameters() {}

    public static void init() {}
}
