package net.errorcraft.itematic.mixin.world.level.block.entity.vault;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VaultServerData.class)
public class VaultServerDataExtender {
    @WrapOperation(
        method = "lambda$static$0",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Codec<ItemStack> useFailableItemStackCodec(Operation<Codec<ItemStack>> original) {
        return ItemStacks.POSSIBLY_FAILED_CODEC;
    }
}
