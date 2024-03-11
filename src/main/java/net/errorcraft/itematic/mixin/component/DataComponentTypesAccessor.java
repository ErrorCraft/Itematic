package net.errorcraft.itematic.mixin.component;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.UnaryOperator;

@Mixin(DataComponentTypes.class)
public interface DataComponentTypesAccessor {
    @Invoker("register")
    static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        throw new AssertionError();
    }
}
