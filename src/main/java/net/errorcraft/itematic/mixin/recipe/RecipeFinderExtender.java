package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StackedItemContents.class)
public class RecipeFinderExtender implements RecipeFinderAccess {
    @Unique
    private Level world;

    @WrapOperation(
        method = {
            "canCraft(Lnet/minecraft/world/item/crafting/Recipe;ILnet/minecraft/world/entity/player/StackedContents$Output;)Z",
            "getBiggestCraftableStack(Lnet/minecraft/world/item/crafting/Recipe;ILnet/minecraft/world/entity/player/StackedContents$Output;)I"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private PlacementInfo getIngredientPlacementUseDynamicRegistry(Recipe<?> instance, Operation<PlacementInfo> original) {
        if (this.world == null) {
            return original.call(instance);
        }

        return ((RecipeAccess) instance).itematic$ingredientPlacement(this.world.registryAccess().lookupOrThrow(Registries.ITEM));
    }

    @Override
    public void itematic$setWorld(Level world) {
        this.world = world;
    }
}
