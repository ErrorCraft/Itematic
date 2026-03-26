package net.errorcraft.itematic.mixin.util.context;

import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ContextParameterMap.class)
public interface ContextParameterMapAccessor {
    @Invoker("<init>")
    static ContextParameterMap create(Map<ContextParameter<?>, ?> map) {
        throw new AssertionError();
    }

    @Accessor("map")
    Map<ContextParameter<?>, Object> itematic$parameters();
}
