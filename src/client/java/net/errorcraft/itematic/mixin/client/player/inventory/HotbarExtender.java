package net.errorcraft.itematic.mixin.client.player.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Hotbar.class)
public class HotbarExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private static DataResult<Tag> doNotUseCodecForEmptyItemStack(Codec<ItemStack> instance, DynamicOps<Tag> ops, Object o) {
        return DataResult.success(new CompoundTag());
    }

    @WrapOperation(
        method = "storeFrom",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DataResult<Tag> useEmptyMapIfItemStackIsEmpty(Codec<ItemStack> instance, DynamicOps<Tag> ops, Object o, Operation<DataResult<Tag>> original) {
        if (((ItemStack) o).isEmpty()) {
            return DataResult.success(new CompoundTag());
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
    private static DataResult<ItemStack> useEmptyItemStackIfDataIsEmpty(Codec<ItemStack> instance, Dynamic<Tag> dynamic, Operation<DataResult<ItemStack>> original) {
        if (dynamic.getValue() instanceof CompoundTag compound && compound.isEmpty()) {
            return DataResult.success(ItemStack.EMPTY);
        }

        return original.call(instance, dynamic);
    }
}
