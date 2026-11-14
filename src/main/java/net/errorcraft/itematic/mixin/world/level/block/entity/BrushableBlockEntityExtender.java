package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.component.BlockBrusher;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityExtender {
    @Shadow
    @Final
    private static int REQUIRED_BRUSHES_TO_BREAK;

    @Unique
    private int sweepCount = REQUIRED_BRUSHES_TO_BREAK;

    @Inject(
        method = "brush",
        at = @At("HEAD")
    )
    private void setSweepCount(long gameTime, ServerLevel level, LivingEntity user, Direction direction, ItemStack brush, CallbackInfoReturnable<Boolean> info) {
        this.sweepCount = BlockBrusher.BRUSH.get().sweepCount();
    }

    @ModifyConstant(
        method = "brush",
        constant = @Constant(
            intValue = 10
        )
    )
    private int useDataComponent(int constant) {
        return this.sweepCount;
    }

    @WrapOperation(
        method = {
            "getUpdateTag",
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

    @Definition(id = "brushCount", field = "Lnet/minecraft/world/level/block/entity/BrushableBlockEntity;brushCount:I")
    @Expression("this.brushCount < ?")
    @WrapOperation(
        method = "getCompletionState",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean useDataComponent(int left, int right, Operation<Boolean> original) {
        return ((float) left / this.sweepCount) < ((float) right / REQUIRED_BRUSHES_TO_BREAK);
    }
}
