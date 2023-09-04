package net.errorcraft.itematic.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.recipe.RecipeCodecsAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;

public class RecipeUtil {
    public static final Codec<RegistryEntry<Item>> CRAFTING_RESULT_CODEC = Codecs.validate(
        RegistryFixedCodec.of(RegistryKeys.ITEM),
        item -> item.matchesKey(ItemKeys.AIR) ?
            DataResult.error(RecipeCodecsAccessor::craftingResultErrorProvider) :
            DataResult.success(item)
    );
    public static final Codec<ItemStack> INGREDIENT_CODEC = Codecs.validate(
        RegistryFixedCodec.of(RegistryKeys.ITEM),
        item -> item.matchesKey(ItemKeys.AIR) ?
            DataResult.error(RecipeCodecsAccessor::ingredientErrorProvider) :
            DataResult.success(item)
    ).xmap(ItemStack::new, ItemStack::getRegistryEntry);

    private RecipeUtil() {}
}
