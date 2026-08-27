package net.errorcraft.itematic.mixin.network;

import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FriendlyByteBuf.class)
public class FriendlyByteBufExtender {
    @ModifyArg(
        method = "readWithCodec(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Lnet/minecraft/nbt/NbtAccounter;)Ljava/lang/Object;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private DynamicOps<Tag> useCustomDynamicOpsForParse(DynamicOps<Tag> ops) {
        return this.dynamicOps();
    }

    @ModifyArg(
        method = "writeWithCodec(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)Lnet/minecraft/network/FriendlyByteBuf;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private DynamicOps<Tag> useCustomDynamicOpsForEncodeStart(DynamicOps<Tag> ops) {
        return this.dynamicOps();
    }

    @Unique
    protected DynamicOps<Tag> dynamicOps() {
        return NbtOps.INSTANCE;
    }
}
