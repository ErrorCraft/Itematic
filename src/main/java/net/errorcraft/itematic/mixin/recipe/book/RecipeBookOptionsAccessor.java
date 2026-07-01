package net.errorcraft.itematic.mixin.recipe.book;

import com.mojang.serialization.MapCodec;
import net.minecraft.recipe.book.RecipeBookOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RecipeBookOptionsAccessor {
    @Mixin(RecipeBookOptions.CategoryOption.class)
    interface CategoryOptionAccessor {
        @Invoker("createCodec")
        static MapCodec<RecipeBookOptions.CategoryOption> createCodec(String guiOpenField, String filteringCraftableField) {
            throw new AssertionError();
        }
    }
}
