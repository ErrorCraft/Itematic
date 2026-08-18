package net.errorcraft.itematic.world.item.crafting;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.errorcraft.itematic.world.item.crafting.display.BrewingRecipeDisplay;
import net.errorcraft.itematic.world.item.crafting.display.PotionSlotDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

public class ModifyBrewingRecipe extends BrewingRecipe<Potion> {
    public ModifyBrewingRecipe(String group, Holder<Potion> base, Ingredient reagent, Holder<Potion> result, int brewingTime) {
        super(group, base, reagent, result, brewingTime);
    }

    public ModifyBrewingRecipe(Holder<Potion> base, Ingredient reagent, Holder<Potion> result) {
        super("", base, reagent, result, DEFAULT_BREWING_TIME);
    }

    @Override
    protected boolean matches(ItemStack base) {
        return base.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .is(this.base());
    }

    @Override
    protected ItemStack assemble(ItemStack base) {
        return PotionContentsUtil.setPotion(base.copyWithCount(1), this.result());
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_MODIFY;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public PlacementInfo itematic$placementInfo(HolderGetter<Item> items) {
        return PlacementInfo.createFromOptionals(List.of(
            items.get(ItematicItemTags.BREWING_INPUTS).map(Ingredient::of),
            Optional.of(this.reagent())
        ));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ItematicRecipeBookCategories.BREWING_MODIFY;
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new BrewingRecipeDisplay(
                new PotionSlotDisplay(this.base()),
                this.reagent().display(),
                new PotionSlotDisplay(this.result()),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.BREWING_STAND))
            )
        );
    }

    public static class Serializer implements RecipeSerializer<ModifyBrewingRecipe> {
        private static final MapCodec<ModifyBrewingRecipe> CODEC = createCodec(
            Registries.POTION,
            ModifyBrewingRecipe::new
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, ModifyBrewingRecipe> PACKET_CODEC = createPacketCodec(
            Registries.POTION,
            ModifyBrewingRecipe::new
        );

        @Override
        public MapCodec<ModifyBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ModifyBrewingRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}
