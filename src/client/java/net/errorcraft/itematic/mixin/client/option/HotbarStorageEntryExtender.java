package net.errorcraft.itematic.mixin.client.option;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HotbarStorageEntry.class)
public class HotbarStorageEntryExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static DataResult<NbtElement> encodeStartForEmptyItemStackDoNotUseCodec(Codec<ItemStack> instance, DynamicOps<NbtElement> ops, Object o) {
        return DataResult.success(new NbtCompound());
    }

    @WrapOperation(
        method = "serialize",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DataResult<NbtElement> useEmptyIfItemStackIsEmpty(Codec<ItemStack> instance, DynamicOps<NbtElement> ops, Object o, Operation<DataResult<NbtElement>> original) {
        if (((ItemStack) o).isEmpty()) {
            return DataResult.success(new NbtCompound());
        }
        return original.call(instance, ops, o);
    }

    @WrapOperation(
        method = "method_56840",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static DataResult<ItemStack> useEmptyItemStackIfDataIsEmpty(Codec<ItemStack> instance, Dynamic<NbtElement> dynamic, Operation<DataResult<ItemStack>> original) {
        if (dynamic.getValue() instanceof NbtCompound nbt && nbt.isEmpty()) {
            return DataResult.success(ItemStack.EMPTY);
        }
        return original.call(instance, dynamic);
    }
}
