package net.errorcraft.itematic.mixin.data.server.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.data.server.recipe.RecipeProviderAccess;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeProvider.class)
public abstract class RecipeProviderExtender implements RecipeProviderAccess {
    @Shadow
    public abstract void generate(RecipeExporter exporter);

    @Redirect(
        method = "run(Lnet/minecraft/data/DataWriter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Ljava/util/concurrent/CompletableFuture;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/data/server/recipe/RecipeProvider;generate(Lnet/minecraft/data/server/recipe/RecipeExporter;)V"
        )
    )
    private void passRegistryLookup(RecipeProvider instance, RecipeExporter exporter, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        this.itematic$generate(lookup, exporter);
    }

    @Override
    public void itematic$generate(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter) {
        this.generate(exporter);
    }
}
