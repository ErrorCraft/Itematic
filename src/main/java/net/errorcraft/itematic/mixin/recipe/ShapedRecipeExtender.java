package net.errorcraft.itematic.mixin.recipe;

import com.google.gson.JsonObject;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeExtender {
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

    @Redirect(
        method = "getItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"
        )
    )
    private static Optional<Item> getItemGetOrEmptyUseDynamicRegistry(DefaultedRegistry<Item> instance, Identifier identifier) {
        return RecipeSerializerUtil.getItemRegistry().getOrEmpty(identifier);
    }

    @Redirect(
        method = "getItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Items;AIR:Lnet/minecraft/item/Item;"
        )
    )
    private static Item getItemGetAirUseDynamicRegistry() {
        return RecipeSerializerUtil.getItemRegistry().get(ItemKeys.AIR);
    }
}
