package errorcraft.itematic.mixin.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeExtender {
//    @Inject(
//        method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z",
//        at = @At(
//            value = "INVOKE_ASSIGN",
//            target = "Lnet/minecraft/recipe/RecipeMatcher;<init>()V",
//            shift = At.Shift.BY,
//            by = 2
//        ),
//        locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private void matchesSetDynamicRegistryManager(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> info, RecipeMatcher recipeMatcher) {
//        recipeMatcher.setRegistryManager(world.getRegistryManager());
//    }

    @ModifyVariable(
        method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z",
        at = @At("STORE")
    )
    private RecipeMatcher x(RecipeMatcher value, CraftingInventory craftingInventory, World world) {
        value.setRegistryManager(world.getRegistryManager());
        return value;
    }
}
