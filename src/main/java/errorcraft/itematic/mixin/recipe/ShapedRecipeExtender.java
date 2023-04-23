package errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Redirect(
        method = "outputFromJson",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack outputFromJsonUseRegistryEntry(ItemConvertible item, int count, JsonObject json) {
        RegistryEntry<Item> itemEntry = RecipeSerializerUtil.getItem(json);
        return new ItemStack(itemEntry, count);
    }
}
