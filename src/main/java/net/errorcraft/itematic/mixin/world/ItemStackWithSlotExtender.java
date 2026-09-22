package net.errorcraft.itematic.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStackWithSlot.class)
public class ItemStackWithSlotExtender {
    @WrapOperation(
        method = "lambda$static$0",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;MAP_CODEC:Lcom/mojang/serialization/MapCodec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static MapCodec<ItemStack> useFailableItemStackMapCodec(Operation<MapCodec<ItemStack>> original) {
        return ItemStacks.POSSIBLY_FAILED_MAP_CODEC;
    }
}
