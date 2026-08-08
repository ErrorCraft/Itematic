package net.errorcraft.itematic.recipe.brewing;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.display.BrewingRecipeDisplay;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

public class AmplifyBrewingRecipe extends BrewingRecipe<Item> {
    public AmplifyBrewingRecipe(String group, Holder<Item> base, Ingredient reagent, Holder<Item> result, int brewingTime) {
        super(group, base, reagent, result, brewingTime);
    }

    public AmplifyBrewingRecipe(Holder<Item> base, Ingredient reagent, Holder<Item> result) {
        super("", base, reagent, result, DEFAULT_BREWING_TIME);
    }

    @Override
    protected boolean matches(ItemStack base) {
        return base.is(this.base());
    }

    @Override
    protected ItemStack assemble(ItemStack base) {
        return base.split(1).itematic$copyWithItem(this.result());
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_AMPLIFY;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.createFromOptionals(List.of(
            Optional.of(Ingredient.of(HolderSet.direct(this.base()))),
            Optional.of(this.reagent())
        ));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ItematicRecipeBookCategories.BREWING_AMPLIFY;
    }

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new BrewingRecipeDisplay(
                new SlotDisplay.ItemStackSlotDisplay(displayStack(this.base())),
                this.reagent().display(),
                new SlotDisplay.ItemStackSlotDisplay(displayStack(this.result())),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.BREWING_STAND))
            )
        );
    }

    private static ItemStack displayStack(Holder<Item> item) {
        return PotionContentsUtil.setPotion(new ItemStack(item), Potions.WATER);
    }

    public static class Serializer implements RecipeSerializer<AmplifyBrewingRecipe> {
        private static final MapCodec<AmplifyBrewingRecipe> CODEC = createCodec(
            Registries.ITEM,
            AmplifyBrewingRecipe::new
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, AmplifyBrewingRecipe> PACKET_CODEC = createPacketCodec(
            Registries.ITEM,
            AmplifyBrewingRecipe::new
        );

        @Override
        public MapCodec<AmplifyBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AmplifyBrewingRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
