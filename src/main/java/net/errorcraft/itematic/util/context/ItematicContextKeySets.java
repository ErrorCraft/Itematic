package net.errorcraft.itematic.util.context;

import net.minecraft.core.Registry;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ItematicContextKeySets {
    public static final ContextKeySet GLIDER_BOOST = new ContextKeySet.Builder()
        .required(LootContextParams.THIS_ENTITY)
        .required(LootContextParams.ORIGIN)
        .required(LootContextParams.TOOL)
        .build();

    private ItematicContextKeySets() {}

    public static void bootstrap(Registry<ContextKeySet> registry) {
        Registry.register(registry, "glider_boost", GLIDER_BOOST);
    }
}
