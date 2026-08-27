package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Unique
    private static final ScopedValue<HolderLookup.RegistryLookup<Item>> ITEMS = ScopedValue.newInstance();

    @WrapMethod(
        method = "finalizeRecipeLoading"
    )
    private void passItemLookup(FeatureFlagSet enabledFlags, Operation<Void> original) {
        ScopedValue.where(ITEMS, this.registries.lookupOrThrow(Registries.ITEM))
            .run(() -> original.call(enabledFlags));
    }

    @WrapOperation(
        method = {
            "lambda$finalizeRecipeLoading$1",
            "unpackRecipeInfo"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private static PlacementInfo placementInfoUseDynamicRegistry(Recipe<?> instance, Operation<PlacementInfo> original) {
        return instance.itematic$placementInfo(ITEMS.get());
    }

    @WrapOperation(
        method = "unpackRecipeInfo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;display()Ljava/util/List;"
        )
    )
    private static List<RecipeDisplay> displayUseDynamicRegistry(Recipe<?> instance, Operation<List<RecipeDisplay>> original) {
        return instance.itematic$display(ITEMS.get());
    }
}
