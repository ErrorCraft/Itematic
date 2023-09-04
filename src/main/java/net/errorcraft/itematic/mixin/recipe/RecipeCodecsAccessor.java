package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.recipe.RecipeCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeCodecs.class)
public interface RecipeCodecsAccessor {
    @Invoker("method_53719")
    static String craftingResultErrorProvider() {
        throw new AssertionError();
    }

    @Invoker("method_53716")
    static String ingredientErrorProvider() {
        throw new AssertionError();
    }
}
