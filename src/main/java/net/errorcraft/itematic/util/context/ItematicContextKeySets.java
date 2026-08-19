package net.errorcraft.itematic.util.context;

import net.errorcraft.itematic.mixin.world.level.storage.loot.parameters.LootContextParamSetsAccessor;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ItematicContextKeySets {
    public static final ContextKeySet TRADE = LootContextParamSetsAccessor.register("trade", builder ->
        builder.required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
    );

    private ItematicContextKeySets() {}

    public static void init() {}
}
