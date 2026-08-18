package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.world.item.crafting.IngredientAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
        ).apply(instance, IngredientExtender::setRemainder));
        return Codec.either(original, fullCodec)
            .xmap(Either::unwrap, IngredientExtender::wrap);
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
    private static Ingredient setRemainder(Ingredient ingredient, Optional<ItemStack> remainder) {
        ingredient.itematic$setRemainder(remainder);
        return ingredient;
    }

    @Unique
    private static Either<Ingredient, Ingredient> wrap(Ingredient ingredient) {
        if (ingredient.itematic$remainder().isEmpty()) {
            return Either.left(ingredient);
        }

        return Either.right(ingredient);
    }
}
