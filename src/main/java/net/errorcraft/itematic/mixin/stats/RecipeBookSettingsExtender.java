package net.errorcraft.itematic.mixin.stats;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.stats.RecipeBookSettingsAccess;
import net.errorcraft.itematic.stats.ItematicRecipeBookSettings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Mixin(RecipeBookSettings.class)
public class RecipeBookSettingsExtender implements RecipeBookSettingsAccess {
    @Unique
    private RecipeBookSettings.TypeSettings brewing = RecipeBookSettings.TypeSettings.DEFAULT;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private static StreamCodec<FriendlyByteBuf, RecipeBookSettings> addExtraCompositeStreamCodecEntries(StreamCodec<FriendlyByteBuf, RecipeBookSettings> original) {
        return StreamCodec.composite(
            original, Function.identity(),
            RecipeBookSettings.TypeSettings.STREAM_CODEC, RecipeBookSettings::itematic$brewing,
            RecipeBookSettingsExtender::setBrewing
        );
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"
        )
    )
    private static MapCodec<RecipeBookSettings> addExtraMapCodecFields(MapCodec<RecipeBookSettings> original) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            original.forGetter(Function.identity()),
            ItematicRecipeBookSettings.BREWING_MAP_CODEC.forGetter(RecipeBookSettings::itematic$brewing)
        ).apply(instance, RecipeBookSettingsExtender::setBrewing));
    }

    @WrapMethod(
        method = "getSettings"
    )
    private RecipeBookSettings.TypeSettings checkBrewing(RecipeBookType type, Operation<RecipeBookSettings.TypeSettings> original) {
        if (type == RecipeBookType.ITEMATIC_BREWING) {
            return this.brewing;
        }

        return original.call(type);
    }

    @WrapMethod(
        method = "updateSettings"
    )
    private void checkBrewing(RecipeBookType recipeBookType, UnaryOperator<RecipeBookSettings.TypeSettings> operator, Operation<Void> original) {
        if (recipeBookType == RecipeBookType.ITEMATIC_BREWING) {
            this.brewing = operator.apply(this.brewing);
            return;
        }

        original.call(recipeBookType, operator);
    }

    @ModifyReturnValue(
        method = "copy",
        at = @At("TAIL")
    )
    private RecipeBookSettings setBrewing(RecipeBookSettings original) {
        original.itematic$setBrewing(this.brewing);
        return original;
    }

    @Inject(
        method = "replaceFrom",
        at = @At("TAIL")
    )
    private void copyBrewing(RecipeBookSettings other, CallbackInfo info) {
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
    private static RecipeBookSettings setBrewing(RecipeBookSettings settings, RecipeBookSettings.TypeSettings brewing) {
        settings.itematic$setBrewing(brewing);
        return settings;
    }
}
