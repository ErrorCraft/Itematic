package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityExtender extends BlockEntity {
    public DecoratedPotBlockEntityExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtCompound;get(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;"
        )
    )
    private <T> Optional<T> getUseRegistryOps(NbtCompound instance, String key, Codec<T> codec, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return instance.get(key, codec, lookup.getOps(NbtOps.INSTANCE));
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtCompound;put(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V"
        )
    )
    private <T> void putUseRegistryOps(NbtCompound instance, String key, Codec<T> codec, T value, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        instance.put(key, codec, lookup.getOps(NbtOps.INSTANCE), value);
    }
}
