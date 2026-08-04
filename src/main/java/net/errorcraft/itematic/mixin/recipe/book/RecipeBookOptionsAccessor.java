package net.errorcraft.itematic.mixin.recipe.book;

import com.mojang.serialization.MapCodec;
import net.minecraft.stats.RecipeBookSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RecipeBookOptionsAccessor {
    @Mixin(RecipeBookSettings.TypeSettings.class)
    interface CategoryOptionAccessor {
        @Invoker("codec")
        static MapCodec<RecipeBookSettings.TypeSettings> createCodec(String guiOpenField, String filteringCraftableField) {
            throw new AssertionError();
        }
    }
}
