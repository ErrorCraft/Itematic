package net.errorcraft.itematic.recipe.brewing;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.display.BrewingRecipeDisplay;
import net.errorcraft.itematic.recipe.display.slot.PotionSlotDisplay;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potion;
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

import java.util.List;

public class ModifyBrewingRecipe extends BrewingRecipe<Potion> {
    public ModifyBrewingRecipe(String group, RegistryEntry<Potion> base, Ingredient addition, RegistryEntry<Potion> result) {
        super(group, base, addition, result);
    }

    @Override
    protected boolean matches(ItemStack base) {
        return base.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)
            .matches(this.base());
    }

    @Override
    protected ItemStack craft(ItemStack base) {
        return PotionContentsComponentUtil.setPotion(base.copyWithCount(1), this.result());
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_MODIFY;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE; // todo
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return ItematicRecipeBookCategories.BREWING_MODIFY;
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new BrewingRecipeDisplay(
                new PotionSlotDisplay(this.base()),
                this.addition().toDisplay(),
                new PotionSlotDisplay(this.result()),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.BREWING_STAND))
            )
        );
    }

    public static class Serializer implements RecipeSerializer<ModifyBrewingRecipe> {
        private static final MapCodec<ModifyBrewingRecipe> CODEC = createCodec(
            RegistryKeys.POTION,
            ModifyBrewingRecipe::new
        );
        private static final PacketCodec<RegistryByteBuf, ModifyBrewingRecipe> PACKET_CODEC = createPacketCodec(
            RegistryKeys.POTION,
            ModifyBrewingRecipe::new
        );

        @Override
        public MapCodec<ModifyBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ModifyBrewingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
