package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.class_8566;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeExtender {
    @ModifyVariable(
        method = "matches(Lnet/minecraft/class_8566;Lnet/minecraft/world/World;)Z",
        at = @At("STORE")
    )
    private RecipeMatcher matchesRecipeMatcherSetRegistryManager(RecipeMatcher value, class_8566 arg, World world) {
        value.setRegistryManager(world.getRegistryManager());
        return value;
    }
}
