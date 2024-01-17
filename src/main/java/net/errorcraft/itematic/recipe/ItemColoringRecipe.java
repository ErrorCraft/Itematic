package net.errorcraft.itematic.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DyeItemComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record ItemColoringRecipe(CraftingRecipeCategory category, Ingredient ingredient, DyeColor color, ItemStack result) implements CraftingRecipe {
    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean foundIngredient = false;
        boolean foundColor = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (this.ingredient.test(stack)) {
                if (foundIngredient) {
                    return false;
                }
                foundIngredient = true;
                continue;
            }
            if (this.isExpectedColor(stack)) {
                if (foundColor) {
                    return false;
                }
                foundColor = true;
                continue;
            }
            return false;
        }
        return foundIngredient && foundColor;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack result = this.result.copy();
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack stack = inventory.getStack(i);
            if (!this.ingredient.test(stack)) {
                continue;
            }
            if (stack.hasNbt()) {
                result.setNbt(stack.getNbt().copy());
            }
            break;
        }
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return this.result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ItematicRecipeSerializers.ITEM_COLORING;
    }

    private boolean isExpectedColor(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.DYE)
            .map(DyeItemComponent::color)
            .map(color -> this.color == color)
            .orElse(false);
    }

    public static class Serializer implements RecipeSerializer<ItemColoringRecipe> {
        private static final Codec<ItemColoringRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(ItemColoringRecipe::category),
            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(ItemColoringRecipe::ingredient),
            DyeColor.CODEC.fieldOf("color").forGetter(ItemColoringRecipe::color),
            RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("result").xmap(ItemStack::new, ItemStack::getRegistryEntry).forGetter(ItemColoringRecipe::result)
        ).apply(instance, ItemColoringRecipe::new));

        @Override
        public Codec<ItemColoringRecipe> codec() {
            return CODEC;
        }

        @Override
        public ItemColoringRecipe read(PacketByteBuf buf) {
            CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);
            Ingredient input = Ingredient.fromPacket(buf);
            DyeColor color = buf.readEnumConstant(DyeColor.class);
            ItemStack result = buf.readItemStack();
            return new ItemColoringRecipe(category, input, color, result);
        }

        @Override
        public void write(PacketByteBuf buf, ItemColoringRecipe recipe) {
            buf.writeEnumConstant(recipe.category);
            recipe.ingredient.write(buf);
            buf.writeEnumConstant(recipe.color);
            buf.writeItemStack(recipe.result);
        }
    }
}
