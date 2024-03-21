package net.errorcraft.itematic.mixin.client.toast;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeToast.class)
public class RecipeToastExtender {
    @Redirect(
        method = "draw",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Recipe;createIcon()Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack createIconUseDynamicRegistry(Recipe<?> instance, @Local(argsOnly = true) ToastManager manager) {
        ClientWorld world = manager.getClient().world;
        if (world == null) {
            return ItemStack.EMPTY;
        }
        return ((RecipeAccess) instance).itematic$createIcon(world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ITEM));
    }
}
