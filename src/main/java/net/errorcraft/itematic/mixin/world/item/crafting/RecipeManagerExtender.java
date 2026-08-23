package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Unique
    private static HolderLookup.RegistryLookup<Item> items;

    @WrapMethod(
        method = "finalizeRecipeLoading"
    )
    private void temporarilyStoreItemLookup(FeatureFlagSet enabledFlags, Operation<Void> original) {
        items = this.registries.lookupOrThrow(Registries.ITEM);
        original.call(enabledFlags);
        items = null;
    }

    @Redirect(
        method = {
            "lambda$finalizeRecipeLoading$1",
            "unpackRecipeInfo"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private static PlacementInfo placementInfoUseDynamicRegistry(Recipe<?> instance) {
        return instance.itematic$placementInfo(items);
    }

    @Redirect(
        method = "unpackRecipeInfo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;display()Ljava/util/List;"
        )
    )
    private static List<RecipeDisplay> displayUseDynamicRegistry(Recipe<?> instance) {
        return instance.itematic$display(items);
    }
}
