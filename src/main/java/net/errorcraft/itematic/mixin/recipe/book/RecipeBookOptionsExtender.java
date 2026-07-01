package net.errorcraft.itematic.mixin.recipe.book;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.book.RecipeBookOptionsAccess;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookOptions;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.book.RecipeBookOptions;
import net.minecraft.recipe.book.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Mixin(RecipeBookOptions.class)
public class RecipeBookOptionsExtender implements RecipeBookOptionsAccess {
    @Unique
    private RecipeBookOptions.CategoryOption brewing = RecipeBookOptions.CategoryOption.DEFAULT;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private static PacketCodec<PacketByteBuf, RecipeBookOptions> addBrewingFieldPacketCodec(PacketCodec<PacketByteBuf, RecipeBookOptions> original) {
        return PacketCodec.tuple(
            original, Function.identity(),
            RecipeBookOptions.CategoryOption.PACKET_CODEC, RecipeBookOptions::itematic$brewing,
            RecipeBookOptionsExtender::setFields
        );
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"
        )
    )
    private static MapCodec<RecipeBookOptions> addBrewingFieldCodec(MapCodec<RecipeBookOptions> original) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            original.forGetter(Function.identity()),
            ItematicRecipeBookOptions.BREWING_CODEC.forGetter(RecipeBookOptions::itematic$brewing)
        ).apply(instance, RecipeBookOptionsExtender::setFields));
    }

    @Inject(
        method = "getOption",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getOptionCheckBrewing(RecipeBookType type, CallbackInfoReturnable<RecipeBookOptions.CategoryOption> info) {
        if (type == RecipeBookType.ITEMATIC_BREWING) {
            info.setReturnValue(this.brewing);
        }
    }

    @Inject(
        method = "apply",
        at = @At("HEAD"),
        cancellable = true
    )
    private void applyCheckBrewing(RecipeBookType type, UnaryOperator<RecipeBookOptions.CategoryOption> modifier, CallbackInfo info) {
        if (type == RecipeBookType.ITEMATIC_BREWING) {
            this.brewing = modifier.apply(this.brewing);
            info.cancel();
        }
    }

    @ModifyReturnValue(
        method = "copy",
        at = @At("TAIL")
    )
    private RecipeBookOptions setBrewingField(RecipeBookOptions original) {
        original.itematic$setBrewing(this.brewing);
        return original;
    }

    @Inject(
        method = "copyFrom",
        at = @At("TAIL")
    )
    private void copyBrewingField(RecipeBookOptions other, CallbackInfo info) {
        this.brewing = other.itematic$brewing();
    }

    @Override
    public RecipeBookOptions.CategoryOption itematic$brewing() {
        return this.brewing;
    }

    @Override
    public void itematic$setBrewing(RecipeBookOptions.CategoryOption brewing) {
        this.brewing = brewing;
    }

    @Unique
    private static RecipeBookOptions setFields(RecipeBookOptions recipeBookOptions, RecipeBookOptions.CategoryOption brewing) {
        recipeBookOptions.itematic$setBrewing(brewing);
        return recipeBookOptions;
    }
}
