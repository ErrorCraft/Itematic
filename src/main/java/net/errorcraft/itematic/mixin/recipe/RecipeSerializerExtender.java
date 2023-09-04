package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.StonecuttingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(RecipeSerializer.class)
public interface RecipeSerializerExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/RecipeSerializer;register(Ljava/lang/String;Lnet/minecraft/recipe/RecipeSerializer;)Lnet/minecraft/recipe/RecipeSerializer;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=stonecutting"
            )
        )
    )
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static <T extends CuttingRecipe> RecipeSerializer<T> setStonecuttingFactory(RecipeSerializer<T> serializer) {
        ((CuttingRecipe.Serializer<T>) serializer).setFactory((group, ingredient, result, count) -> {
            StonecuttingRecipe recipe = new StonecuttingRecipe(group, ingredient, null, 0);
            recipe.setResult(result, count);
            return recipe;
        });
        return serializer;
    }
}
