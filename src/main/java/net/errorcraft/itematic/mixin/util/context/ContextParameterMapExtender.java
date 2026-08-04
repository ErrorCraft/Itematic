package net.errorcraft.itematic.mixin.util.context;

import net.errorcraft.itematic.access.util.context.ContextParameterMapAccess;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

public class ContextParameterMapExtender {
    @Mixin(ContextMap.Builder.class)
    public static class BuilderExtender implements ContextParameterMapAccess.BuilderAccess {
        @Shadow
        @Final
        private Map<ContextKey<?>, Object> params;

        @Override
        public void itematic$copy(ContextMap other) {
            this.params.putAll(((ContextParameterMapAccessor) other).itematic$parameters());
        }

        @Override
        public ContextMap itematic$build() {
            return ContextParameterMapAccessor.create(this.params);
        }
    }
}
