package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityExtender {
    @Shadow
    private ItemStack book;

    @WrapMethod(
        method = "hasBook"
    )
    private boolean alsoCheckWritableAndTextHolderItemBehavior(Operation<Boolean> original) {
        if (!this.book.itematic$hasBehavior(ItemBehaviorType.WRITABLE) && !this.book.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return false;
        }

        return original.call();
    }

    @WrapMethod(
        method = "resolveBook"
    )
    private ItemStack alsoCheckTextHolderItemBehavior(ItemStack book, Player player, Operation<ItemStack> original) {
        if (!this.book.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return this.book;
        }

        return book;
    }

    @WrapOperation(
        method = {
            "loadAdditional",
            "saveAdditional"
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
