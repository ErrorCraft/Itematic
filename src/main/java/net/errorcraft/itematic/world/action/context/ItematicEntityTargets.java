package net.errorcraft.itematic.world.action.context;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.loot.context.LootContext;

public class ItematicEntityTargets {
    public static final LootContext.EntityTarget TARGET_ENTITY = ClassTinkerers.getEnum(LootContext.EntityTarget.class, "ITEMATIC$TARGET_ENTITY");

    private ItematicEntityTargets() {}
}
