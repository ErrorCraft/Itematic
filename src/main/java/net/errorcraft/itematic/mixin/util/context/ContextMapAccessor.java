package net.errorcraft.itematic.mixin.util.context;

import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ContextMap.class)
public interface ContextMapAccessor {
    @Invoker("<init>")
    static ContextMap create(Map<ContextKey<?>, ?> params) {
        throw new AssertionError();
    }

    @Accessor("params")
    Map<ContextKey<?>, Object> itematic$params();
}
