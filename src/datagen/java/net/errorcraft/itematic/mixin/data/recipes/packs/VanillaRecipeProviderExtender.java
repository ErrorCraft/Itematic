package net.errorcraft.itematic.mixin.data.recipes.packs;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import org.spongepowered.asm.mixin.Mixin;

public class VanillaRecipeProviderExtender {
    @Mixin(targets = "net/minecraft/data/recipes/packs/VanillaRecipeProvider$1")
    public static class Bootstrapper {
        @WrapMethod(
            method = "run"
        )
        private void doNotRunRecipeDataGenerationYourLogsWillDie(MultiRegistryBootstrap.BootstrapGetter registries, Operation<Void> original) {}
    }
}
