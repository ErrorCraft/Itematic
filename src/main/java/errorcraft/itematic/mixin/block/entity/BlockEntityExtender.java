package errorcraft.itematic.mixin.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityExtender {
    @Redirect(
        method = "method_17897",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
        ),
        remap = false
    )
    private static void yes(BlockEntity instance, NbtCompound nbt) {}
}
