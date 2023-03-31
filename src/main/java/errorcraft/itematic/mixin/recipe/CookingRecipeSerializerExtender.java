package errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CookingRecipeSerializer.class)
public class CookingRecipeSerializerExtender<T extends AbstractCookingRecipe> {
    @Redirect(
        method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"
        )
    )
    private Optional<Item> readUseDynamicRegistry(DefaultedRegistry<Item> instance, Identifier identifier) {
        return RecipeSerializerUtil.getItemRegistry().getOrEmpty(identifier);
    }

    @Redirect(
        method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack readUseRegistryEntry(ItemConvertible item, Identifier identifier, JsonObject jsonObject) {
        return new ItemStack(RecipeSerializerUtil.getResultItem(jsonObject));
    }
}
