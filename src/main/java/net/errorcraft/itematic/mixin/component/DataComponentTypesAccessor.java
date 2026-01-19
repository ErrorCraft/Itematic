package net.errorcraft.itematic.mixin.component;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.UnaryOperator;

@Mixin(DataComponentTypes.class)
public interface DataComponentTypesAccessor {
    @Invoker("register")
    static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        throw new AssertionError();
    }
}
