package net.errorcraft.itematic.recipe.brewing;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.recipe.ItematicRecipeSerializers;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.collection.DefaultedList;

import java.util.stream.Stream;

public class ModifyBrewingRecipe extends BrewingRecipe<Potion> {
    public ModifyBrewingRecipe(String group, RegistryEntry<Potion> base, Ingredient reagent, RegistryEntry<Potion> result, int brewingTime) {
        super(group, base, reagent, result, brewingTime);
    }

    public ModifyBrewingRecipe(RegistryEntry<Potion> base, Ingredient reagent, RegistryEntry<Potion> result) {
        super("", base, reagent, result, DEFAULT_BREWING_TIME);
    }

    @Override
    public Ingredient inputIngredient(RegistryEntryLookup<Item> items) {
        return Ingredient.ofStacks(this.getInputStacks(items));
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
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
        return lookup.getWrapperOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.POTION)
            .map(ItemStack::new)
            .map(stack -> PotionContentsComponentUtil.setPotion(stack, this.result()))
            .orElse(ItemStack.EMPTY);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(2);
        ingredients.add(this.reagent());
        ingredients.add(Ingredient.fromTag(ItematicItemTags.BREWING_INPUTS));
        return ingredients;
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return ItematicRecipeSerializers.BREWING_MODIFY;
    }

    private Stream<ItemStack> getInputStacks(RegistryEntryLookup<Item> items) {
        return items.getOptional(ItematicItemTags.BREWING_INPUTS)
            .stream()
            .flatMap(RegistryEntryList.ListBacked::stream)
            .map(ItemStack::new)
            .map(stack -> PotionContentsComponentUtil.setPotion(stack, this.base()));
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
