package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.access.util.ActionResultAccess;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ActionResult.class)
public interface ActionResultExtender extends ActionResultAccess {
    // todo remove ActionResultExtender and ActionResultAccess
}
