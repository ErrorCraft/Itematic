package net.errorcraft.itematic.recipe.brewing;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.display.BrewingRecipeDisplay;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.List;
import java.util.Optional;

public class AmplifyBrewingRecipe extends BrewingRecipe<Item> {
    public AmplifyBrewingRecipe(String group, RegistryEntry<Item> base, Ingredient reagent, RegistryEntry<Item> result, int brewingTime) {
        super(group, base, reagent, result, brewingTime);
    }

    public AmplifyBrewingRecipe(RegistryEntry<Item> base, Ingredient reagent, RegistryEntry<Item> result) {
        super("", base, reagent, result, DEFAULT_BREWING_TIME);
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
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_AMPLIFY;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forMultipleSlots(List.of(
            Optional.of(Ingredient.fromTag(RegistryEntryList.of(this.base()))),
            Optional.of(this.reagent())
        ));
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return ItematicRecipeBookCategories.BREWING_AMPLIFY;
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new BrewingRecipeDisplay(
                new SlotDisplay.StackSlotDisplay(displayStack(this.base())),
                this.reagent().toDisplay(),
                new SlotDisplay.StackSlotDisplay(displayStack(this.result())),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.BREWING_STAND))
            )
        );
    }

    private static ItemStack displayStack(RegistryEntry<Item> item) {
        return PotionContentsComponentUtil.setPotion(new ItemStack(item), Potions.WATER);
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
