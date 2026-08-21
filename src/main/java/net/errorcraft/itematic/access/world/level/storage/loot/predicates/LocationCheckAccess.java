package net.errorcraft.itematic.access.world.level.storage.loot.predicates;

import net.errorcraft.itematic.world.action.context.PositionTarget;

public interface LocationCheckAccess {
    default PositionTarget itematic$position() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setPosition(PositionTarget position) {}
}
