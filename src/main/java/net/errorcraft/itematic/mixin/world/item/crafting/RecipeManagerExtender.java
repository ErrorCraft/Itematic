package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerExtender {
    @Unique
    private static final ScopedValue<HolderLookup.RegistryLookup<Item>> ITEMS = ScopedValue.newInstance();

    @Unique
    private HolderLookup.RegistryLookup<Item> items;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setRegistries(HolderLookup.Provider registries, CallbackInfo info) {
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @WrapMethod(
        method = "finalizeRecipeLoading"
    )
    private void passItemLookup(FeatureFlagSet enabledFlags, Operation<Void> original) {
        ScopedValue.where(ITEMS, this.items)
            .run(() -> original.call(enabledFlags));
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
