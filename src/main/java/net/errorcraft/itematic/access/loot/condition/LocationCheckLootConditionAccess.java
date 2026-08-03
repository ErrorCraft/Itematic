package net.errorcraft.itematic.access.loot.condition;

import net.errorcraft.itematic.world.action.context.PositionTarget;

public interface LocationCheckLootConditionAccess {
    default PositionTarget itematic$position() {
        return null;
    }
    default void itematic$setPosition(PositionTarget position) {}
}
