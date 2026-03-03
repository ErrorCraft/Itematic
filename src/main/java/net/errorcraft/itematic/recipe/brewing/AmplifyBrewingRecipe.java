package net.errorcraft.itematic.recipe.brewing;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;

public class AmplifyBrewingRecipe extends BrewingRecipe<Item> {
    public AmplifyBrewingRecipe(String group, RegistryEntry<Item> base, Ingredient reagent, RegistryEntry<Item> result, int brewingTime) {
        super(group, base, reagent, result, brewingTime);
    }

    public AmplifyBrewingRecipe(RegistryEntry<Item> base, Ingredient reagent, RegistryEntry<Item> result) {
        super("", base, reagent, result, DEFAULT_BREWING_TIME);
    }

    @Override
    public Ingredient inputIngredient(RegistryEntryLookup<Item> items) {
        return this.inputIngredient();
    }

    @Override
    protected boolean matches(ItemStack base) {
        return base.itemMatches(this.base());
    }

    @Override
    protected ItemStack craft(ItemStack base) {
        return base.split(1).itematic$copyWithItem(this.result());
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
        return PotionContentsComponentUtil.setPotion(
            new ItemStack(this.result()),
            Potions.WATER
        );
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(2);
        ingredients.add(this.reagent());
        ingredients.add(this.inputIngredient());
        return ingredients;
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_AMPLIFY;
    }

    private Ingredient inputIngredient() {
        return Ingredient.ofStacks(
            PotionContentsComponentUtil.setPotion(
                new ItemStack(this.base()),
                Potions.WATER
            )
        );
    }

    public static class Serializer implements RecipeSerializer<AmplifyBrewingRecipe> {
        private static final MapCodec<AmplifyBrewingRecipe> CODEC = createCodec(
            RegistryKeys.ITEM,
            AmplifyBrewingRecipe::new
        );
        private static final PacketCodec<RegistryByteBuf, AmplifyBrewingRecipe> PACKET_CODEC = createPacketCodec(
            RegistryKeys.ITEM,
            AmplifyBrewingRecipe::new
        );

        @Override
        public MapCodec<AmplifyBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, AmplifyBrewingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
