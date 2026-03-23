package net.errorcraft.itematic.mixin.util.context;

import net.errorcraft.itematic.access.util.context.ContextParameterMapBuilderAccess;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

public class ContextParameterMapExtender {
    @Mixin(ContextParameterMap.Builder.class)
    public static class BuilderExtender implements ContextParameterMapBuilderAccess {
        @Shadow
        @Final
        private Map<ContextParameter<?>, Object> map;

        @Override
        public void itematic$copy(ContextParameterMap other) {
            this.map.putAll(((ContextParameterMapAccessor) other).itematic$parameters());
        }

        @Override
        public ContextParameterMap itematic$build() {
            return ContextParameterMapAccessor.create(this.map);
        }
    }
}
