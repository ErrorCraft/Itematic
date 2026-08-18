package net.errorcraft.itematic.access.util.context;

import net.minecraft.util.context.ContextMap;

public interface ContextMapAccess {
    interface BuilderAccess {
        default void itematic$copy(ContextMap other) {}
        default ContextMap itematic$build() {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
