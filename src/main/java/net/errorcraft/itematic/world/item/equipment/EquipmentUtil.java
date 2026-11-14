package net.errorcraft.itematic.world.item.equipment;

import net.errorcraft.itematic.world.action.context.ActionContext;

public class EquipmentUtil {
    public static final ScopedValue<ActionContext> ON_EQUIP_ACTION_CONTEXT = ScopedValue.newInstance();

    private EquipmentUtil() {}
}
