package net.errorcraft.itematic.mixin.component;

import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.UnaryOperator;

@Mixin(DataComponentTypes.class)
public class DataComponentTypesExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/DataComponentTypes;register(Ljava/lang/String;Ljava/util/function/UnaryOperator;)Lnet/minecraft/component/ComponentType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=glider"
            )
        )
    )
    @SuppressWarnings("unchecked")
    private static <T> ComponentType<T> useCustomGliderDataComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return (ComponentType<T>) ItematicDataComponentTypes.GLIDER;
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/ComponentMap$Builder;add(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/component/ComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/component/DataComponentTypes;MAX_STACK_SIZE:Lnet/minecraft/component/ComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> ComponentMap.Builder doNotAddMaxStackSizeDataComponent(ComponentMap.Builder instance, ComponentType<T> type, T value) {
        return instance;
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/ComponentMap$Builder;add(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/component/ComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/component/DataComponentTypes;RARITY:Lnet/minecraft/component/ComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> ComponentMap.Builder doNotAddRarityDataComponent(ComponentMap.Builder instance, ComponentType<T> type, T value) {
        return instance;
    }
}
