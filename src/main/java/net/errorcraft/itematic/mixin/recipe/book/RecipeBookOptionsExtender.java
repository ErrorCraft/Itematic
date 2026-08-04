package net.errorcraft.itematic.mixin.recipe.book;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.book.RecipeBookOptionsAccess;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Mixin(RecipeBookSettings.class)
public class RecipeBookOptionsExtender implements RecipeBookOptionsAccess {
    @Unique
    private RecipeBookSettings.TypeSettings brewing = RecipeBookSettings.TypeSettings.DEFAULT;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private static StreamCodec<FriendlyByteBuf, RecipeBookSettings> addBrewingFieldPacketCodec(StreamCodec<FriendlyByteBuf, RecipeBookSettings> original) {
        return StreamCodec.composite(
            original, Function.identity(),
            RecipeBookSettings.TypeSettings.STREAM_CODEC, RecipeBookSettings::itematic$brewing,
            RecipeBookOptionsExtender::setFields
        );
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;",
            remap = false
        )
    )
    private static MapCodec<RecipeBookSettings> addBrewingFieldCodec(MapCodec<RecipeBookSettings> original) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            original.forGetter(Function.identity()),
            ItematicRecipeBookOptions.BREWING_CODEC.forGetter(RecipeBookSettings::itematic$brewing)
        ).apply(instance, RecipeBookOptionsExtender::setFields));
    }

    @Inject(
        method = "getSettings",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getOptionCheckBrewing(RecipeBookType type, CallbackInfoReturnable<RecipeBookSettings.TypeSettings> info) {
        if (type == RecipeBookType.ITEMATIC_BREWING) {
            info.setReturnValue(this.brewing);
        }
    }

    @Inject(
        method = "updateSettings",
        at = @At("HEAD"),
        cancellable = true
    )
    private void applyCheckBrewing(RecipeBookType type, UnaryOperator<RecipeBookSettings.TypeSettings> modifier, CallbackInfo info) {
        if (type == RecipeBookType.ITEMATIC_BREWING) {
            this.brewing = modifier.apply(this.brewing);
            info.cancel();
        }
    }

    @ModifyReturnValue(
        method = "copy",
        at = @At("TAIL")
    )
    private RecipeBookSettings setBrewingField(RecipeBookSettings original) {
        original.itematic$setBrewing(this.brewing);
        return original;
    }

    @Inject(
        method = "replaceFrom",
        at = @At("TAIL")
    )
    private void copyBrewingField(RecipeBookSettings other, CallbackInfo info) {
        this.brewing = other.itematic$brewing();
    }

    @Override
    public RecipeBookSettings.TypeSettings itematic$brewing() {
        return this.brewing;
    }

    @Override
    public void itematic$setBrewing(RecipeBookSettings.TypeSettings brewing) {
        this.brewing = brewing;
    }

    @Unique
    private static RecipeBookSettings setFields(RecipeBookSettings recipeBookOptions, RecipeBookSettings.TypeSettings brewing) {
        recipeBookOptions.itematic$setBrewing(brewing);
        return recipeBookOptions;
    }
}
