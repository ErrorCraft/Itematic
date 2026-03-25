package net.errorcraft.itematic.util.context;

import net.errorcraft.itematic.mixin.loot.context.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.context.ContextType;

public class ItematicContextTypes {
    public static final ContextType TRADE = LootContextTypesAccessor.register("trade", builder ->
        builder.require(LootContextParameters.THIS_ENTITY)
            .require(LootContextParameters.ORIGIN)
    );

    private ItematicContextTypes() {}

    public static void init() {}
}
