package net.errorcraft.itematic.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.errorcraft.itematic.world.item.crafting.display.BrewingRecipeDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

public class AmplifyBrewingRecipe extends BrewingRecipe<Item> {
    private static final MapCodec<AmplifyBrewingRecipe> CODEC = codec(
        Registries.ITEM,
        AmplifyBrewingRecipe::new
    );
    private static final StreamCodec<RegistryFriendlyByteBuf, AmplifyBrewingRecipe> STREAM_CODEC = streamCodec(
        Registries.ITEM,
        AmplifyBrewingRecipe::new
    );
    public static final RecipeSerializer<AmplifyBrewingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public AmplifyBrewingRecipe(CommonInfo commonInfo, String group, Holder<Item> base, Ingredient reagent, Holder<Item> result, int brewingTime) {
        super(commonInfo, group, base, reagent, result, brewingTime);
    }

    public AmplifyBrewingRecipe(Holder<Item> base, Ingredient reagent, Holder<Item> result) {
        super(new CommonInfo(true), "", base, reagent, result, DEFAULT_BREWING_TIME);
    }

    @Override
    protected boolean matches(ItemStack base) {
        return base.is(this.base());
    }

    @Override
    protected ItemStack assemble(ItemStack base) {
        return base.split(1).itematic$transmuteCopy(this.result());
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
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new BrewingRecipeDisplay(
                new SlotDisplay.ItemStackSlotDisplay(displayStack(this.base())),
                this.reagent().display(),
                new SlotDisplay.ItemStackSlotDisplay(displayStack(this.result())),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.BREWING_STAND))
            )
        );
    }

    private static ItemStackTemplate displayStack(Holder<Item> item) {
        return ItemStackTemplates.of(
            item,
            DataComponentPatch.builder()
                .set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                .build()
        );
    }
}
