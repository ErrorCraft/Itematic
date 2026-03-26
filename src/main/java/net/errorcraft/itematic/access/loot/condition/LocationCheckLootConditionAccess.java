package net.errorcraft.itematic.access.loot.condition;

import net.errorcraft.itematic.loot.condition.LocationCheckLootConditionExtraFields;

public interface LocationCheckLootConditionAccess {
    default void itematic$setExtraFields(LocationCheckLootConditionExtraFields extraFields) {}
    default LocationCheckLootConditionExtraFields itematic$extraFields() {
        return null;
    }
}
