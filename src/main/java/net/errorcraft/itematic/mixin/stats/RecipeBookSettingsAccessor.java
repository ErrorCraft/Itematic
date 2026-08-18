package net.errorcraft.itematic.mixin.stats;

import com.mojang.serialization.MapCodec;
import net.minecraft.stats.RecipeBookSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface RecipeBookSettingsAccessor {
    @Mixin(RecipeBookSettings.TypeSettings.class)
    interface TypeSettingsAccessor {
        @Invoker("codec")
        static MapCodec<RecipeBookSettings.TypeSettings> codec(String openFieldName, String filteringFieldName) {
            throw new AssertionError();
        }
    }
}
