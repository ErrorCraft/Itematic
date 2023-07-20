package net.errorcraft.itematic.loot.context;

import net.errorcraft.itematic.mixin.loot.context.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;

public class ItematicLootContextTypes {
    public static final LootContextType ACTION = LootContextTypesAccessor.register("action", builder ->
        builder.allow(LootContextParameters.THIS_ENTITY)
            .require(LootContextParameters.ORIGIN)
            .allow(LootContextParameters.TOOL)
    );

    private ItematicLootContextTypes() {}

    public static void init() {}
}
