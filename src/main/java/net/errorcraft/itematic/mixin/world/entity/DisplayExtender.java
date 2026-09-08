package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class DisplayExtender {
    @Mixin(Display.ItemDisplay.class)
    public static class ItemDisplayExtender {
        @WrapOperation(
            method = {
                "readAdditionalSaveData",
                "addAdditionalSaveData"
            },
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private Codec<ItemStack> useFailableItemStackCodec(Operation<Codec<ItemStack>> original) {
            return ItemStacks.POSSIBLY_FAILED_CODEC;
        }
    }
}
