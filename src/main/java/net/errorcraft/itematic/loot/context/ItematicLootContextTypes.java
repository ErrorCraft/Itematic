package net.errorcraft.itematic.loot.context;

import net.errorcraft.itematic.mixin.loot.context.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;

public class ItematicLootContextTypes {
    public static final LootContextType ACTION = LootContextTypesAccessor.register("action", builder ->
        builder.allow(LootContextParameters.THIS_ENTITY)
            .require(LootContextParameters.ORIGIN)
            .allow(LootContextParameters.TOOL)
            .allow(ItematicLootContextParameters.SIDE)
    );
    public static final LootContextType TRADE = LootContextTypesAccessor.register("trade", builder ->
        builder.require(LootContextParameters.THIS_ENTITY)
            .require(LootContextParameters.ORIGIN)
    );

    private ItematicLootContextTypes() {}

    public static void init() {}
}
