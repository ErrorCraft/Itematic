package net.errorcraft.itematic.access.util.context;

import net.minecraft.util.context.ContextParameterMap;

public interface ContextParameterMapBuilderAccess {
    default ContextParameterMap itematic$build() {
        return null;
    }
}
