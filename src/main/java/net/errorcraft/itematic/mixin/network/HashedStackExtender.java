package net.errorcraft.itematic.mixin.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Either;
import net.errorcraft.itematic.network.UnloadableHashedStack;
import net.minecraft.network.HashedPatchMap;
import net.minecraft.network.HashedStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HashedStack.class)
public interface HashedStackExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private static StreamCodec<RegistryFriendlyByteBuf, HashedStack> alsoUseUnloadableHashedStackStreamCodec(StreamCodec<RegistryFriendlyByteBuf, HashedStack> original) {
        return ByteBufCodecs.either(
            UnloadableHashedStack.STREAM_CODEC,
            original
        ).map(
            Either::unwrap,
            hashedStack -> hashedStack instanceof UnloadableHashedStack unloadableHashedStack
                ? Either.left(unloadableHashedStack)
                : Either.right(hashedStack)
        );
    }

    @WrapMethod(
        method = "create"
    )
    private static HashedStack checkSuccessfullyLoaded(ItemStack itemStack, HashedPatchMap.HashGenerator hasher, Operation<HashedStack> original) {
        if (itemStack.itematic$isSuccessfullyLoaded()) {
            return original.call(itemStack, hasher);
        }

        return new UnloadableHashedStack(
            itemStack.itematic$key(),
            itemStack.count(),
            HashedPatchMap.create(itemStack.getComponentsPatch(), hasher)
        );
    }
}
