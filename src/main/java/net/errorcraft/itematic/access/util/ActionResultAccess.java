package net.errorcraft.itematic.access.util;

import net.minecraft.util.ActionResult;

public interface ActionResultAccess {
    default ActionResult itematic$merge(ActionResult other) {
        return null;
    }
}
