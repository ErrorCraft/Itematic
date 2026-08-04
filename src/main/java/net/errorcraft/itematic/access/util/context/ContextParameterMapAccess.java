package net.errorcraft.itematic.access.util.context;

import net.minecraft.util.context.ContextMap;

public interface ContextParameterMapAccess {
    interface BuilderAccess {
        default void itematic$copy(ContextMap other) {}
        default ContextMap itematic$build() {
            return null;
        }
    }
}
