package net.errorcraft.itematic.mixin.recipe.book;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeBookOptions.class)
public class RecipeBookOptionsExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;",
            remap = false
        )
    )
    private static ImmutableMap<RecipeBookCategory, Pair<String, String>> addCustomEntries(ImmutableMap<RecipeBookCategory, Pair<String, String>> original) {
        return ImmutableMap.<RecipeBookCategory, Pair<String, String>>builder()
            .putAll(original)
            .put(
                ItematicRecipeBookCategories.BREWING,
                Pair.of("isBrewingStandGuiOpen", "isBrewingStandFilteringCraftable")
            )
            .build();
    }
}
