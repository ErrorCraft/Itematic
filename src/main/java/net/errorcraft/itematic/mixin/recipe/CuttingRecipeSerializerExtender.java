package net.errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import net.errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CuttingRecipe.Serializer.class)
public class CuttingRecipeSerializerExtender {
    @Redirect(
        method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;"
        )
    )
    private <T> T readReturnNullForRegistryEntryUse(DefaultedRegistry<Item> instance, Identifier identifier) {
        return null;
    }

    @Redirect(
        method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack readUseRegistryEntry(ItemConvertible item, int count, Identifier identifier, JsonObject jsonObject) {
        return new ItemStack(RecipeSerializerUtil.getResultItem(jsonObject), count);
    }
}
