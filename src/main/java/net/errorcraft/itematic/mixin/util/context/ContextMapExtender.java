package net.errorcraft.itematic.mixin.util.context;

import net.errorcraft.itematic.access.util.context.ContextMapAccess;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

public class ContextMapExtender {
    @Mixin(ContextMap.Builder.class)
    public static class BuilderExtender implements ContextMapAccess.BuilderAccess {
        @Shadow
        @Final
        private Map<ContextKey<?>, Object> params;

        @Override
        public void itematic$copy(ContextMap other) {
            this.params.putAll(((ContextMapAccessor) other).itematic$params());
        }

        @Override
        public ContextMap itematic$build() {
            return ContextMapAccessor.create(this.params);
        }
    }
}
