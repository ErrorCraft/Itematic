package errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeExtender {
    @Redirect(
        method = "outputFromJson",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ShapedRecipe;getItem(Lcom/google/gson/JsonObject;)Lnet/minecraft/item/Item;"
        )
    )
    private static Item outputFromJsonDoNotDeserializeItem(JsonObject json) {
        return null;
    }

    @Inject(
        method = "outputFromJson",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private static void outputFromJsonUseRegistryEntry(JsonObject json, CallbackInfoReturnable<ItemStack> info, Item item, int i) {
        RegistryEntry<Item> itemEntry = RecipeSerializerUtil.getItem(json);
        info.setReturnValue(new ItemStack(itemEntry, i));
    }
}
