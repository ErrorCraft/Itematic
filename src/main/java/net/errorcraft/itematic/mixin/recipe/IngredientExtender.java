package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Function;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    @Unique
    private Optional<ItemStack> remainder = Optional.empty();

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<Ingredient> addRemainder(Codec<Ingredient> original) {
        Codec<Ingredient> fullCodec = RecordCodecBuilder.create(instance -> instance.group(
            original.fieldOf("items").forGetter(Function.identity()),
            ItemStack.CODEC.optionalFieldOf("remainder").forGetter(Ingredient::itematic$remainder)
        ).apply(instance, IngredientExtender::setFields));
        return Codec.either(original, fullCodec)
            .xmap(
                either -> either.map(Function.identity(), Function.identity()),
                ingredient -> {
                    if (ingredient.itematic$remainder().isEmpty()) {
                        return Either.left(ingredient);
                    }

                    return Either.right(ingredient);
                }
            );
    }

    @Override
    public Optional<ItemStack> itematic$remainder() {
        return this.remainder;
    }

    @Override
    public void itematic$setRemainder(Optional<ItemStack> remainder) {
        this.remainder = remainder;
    }

    @Unique
    private static Ingredient setFields(Ingredient ingredient, Optional<ItemStack> remainder) {
        ingredient.itematic$setRemainder(remainder);
        return ingredient;
    }
}
