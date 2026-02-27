package net.errorcraft.itematic.mixin.screen;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScreenHandlerType.class)
public interface ScreenHandlerTypeAccessor {
    @Invoker("register")
    static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        throw new AssertionError();
    }
}
