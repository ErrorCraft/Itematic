package net.errorcraft.itematic.mixin.core.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.equipment.Glider;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.UnaryOperator;

@Mixin(DataComponents.class)
public class DataComponentsExtender {
    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponents;register(Ljava/lang/String;Ljava/util/function/UnaryOperator;)Lnet/minecraft/core/component/DataComponentType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=glider"
            )
        )
    )
    private static DataComponentType<Glider> useCustomGliderDataComponent(String id, UnaryOperator<DataComponentType.Builder<Unit>> builder, Operation<DataComponentType<Unit>> original) {
        return ItematicDataComponents.GLIDER;
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentMap$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/component/DataComponents;MAX_STACK_SIZE:Lnet/minecraft/core/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> DataComponentMap.Builder doNotAddMaxStackSizeDataComponent(DataComponentMap.Builder instance, DataComponentType<T> type, @Nullable T value, Operation<DataComponentMap.Builder> original) {
        return instance;
    }

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentMap$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/component/DataComponents;RARITY:Lnet/minecraft/core/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> DataComponentMap.Builder doNotAddRarityDataComponent(DataComponentMap.Builder instance, DataComponentType<T> type, @Nullable T value, Operation<DataComponentMap.Builder> original) {
        return instance;
    }
}
