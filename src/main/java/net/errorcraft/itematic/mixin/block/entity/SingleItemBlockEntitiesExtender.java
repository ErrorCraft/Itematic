package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ JukeboxBlockEntity.class, LecternBlockEntity.class, DecoratedPotBlockEntity.class })
public abstract class SingleItemBlockEntitiesExtender extends BlockEntity {
    public SingleItemBlockEntitiesExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack fromNbtUseDynamicRegistry(NbtCompound nbt, @Local(argsOnly = true) RegistryWrapper.WrapperLookup registryLookup) {
        return ItemStackUtil.fromNbt(nbt, registryLookup);
    }
}
