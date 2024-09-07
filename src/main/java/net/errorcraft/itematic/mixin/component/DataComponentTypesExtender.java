package net.errorcraft.itematic.mixin.component;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DataComponentTypes.class)
public class DataComponentTypesExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/ComponentMap$Builder;add(Lnet/minecraft/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/component/ComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/component/DataComponentTypes;MAX_STACK_SIZE:Lnet/minecraft/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> ComponentMap.Builder doNotAddMaxStackSizeDataComponent(ComponentMap.Builder instance, DataComponentType<T> type, T value) {
        return instance;
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/ComponentMap$Builder;add(Lnet/minecraft/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/component/ComponentMap$Builder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/component/DataComponentTypes;RARITY:Lnet/minecraft/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static <T> ComponentMap.Builder doNotAddRaritySizeDataComponent(ComponentMap.Builder instance, DataComponentType<T> type, T value) {
        return instance;
    }
}
