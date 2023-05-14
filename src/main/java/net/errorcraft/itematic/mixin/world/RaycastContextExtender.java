package net.errorcraft.itematic.mixin.world;

import net.minecraft.fluid.FluidState;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.function.Predicate;

public class RaycastContextExtender {
    @Mixin(RaycastContext.FluidHandling.class)
    public static class FluidHandlingExtender implements StringIdentifiable {
        private String name;

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void initSetNameField(String string, int i, Predicate<FluidState> predicate, CallbackInfo info) {
            this.name = string.toLowerCase(Locale.ROOT);
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
