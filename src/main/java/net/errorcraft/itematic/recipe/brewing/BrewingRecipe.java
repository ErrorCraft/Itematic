package net.errorcraft.itematic.recipe.brewing;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class BrewingRecipe<T> implements Recipe<BrewingRecipeInput>, RecipeAccess {
    private final String group;
    private final RegistryEntry<T> base;
    private final Ingredient reagent;
    private final RegistryEntry<T> result;

    protected BrewingRecipe(String group, RegistryEntry<T> base, Ingredient reagent, RegistryEntry<T> result) {
        this.group = group;
        this.base = base;
        this.reagent = reagent;
        this.result = result;
    }

    protected static <T, R extends BrewingRecipe<T>> MapCodec<R> createCodec(RegistryKey<Registry<T>> registry, Function4<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, R> creator) {
        Codec<RegistryEntry<T>> entryCodec = RegistryFixedCodec.of(registry);
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(R::getGroup),
            entryCodec.fieldOf("base").forGetter(R::base),
            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("reagent").forGetter(R::reagent),
            entryCodec.fieldOf("result").forGetter(R::result)
        ).apply(instance, creator));
    }

    protected static <T, R extends BrewingRecipe<T>> PacketCodec<RegistryByteBuf, R> createPacketCodec(RegistryKey<Registry<T>> registry, Function4<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, R> creator) {
        PacketCodec<RegistryByteBuf, RegistryEntry<T>> entryPacketCodec = PacketCodecs.registryEntry(registry);
        return PacketCodec.tuple(
            PacketCodecs.STRING, R::getGroup,
            entryPacketCodec, R::base,
            Ingredient.PACKET_CODEC, R::reagent,
            entryPacketCodec, R::result,
            creator
        );
    }

    @Override
    public boolean matches(BrewingRecipeInput input, World world) {
        return this.reagent.test(input.reagent()) && this.matches(input.base());
    }

    @Override
    public ItemStack craft(BrewingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return this.craft(input.base());
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeType<? extends Recipe<BrewingRecipeInput>> getType() {
        return ItematicRecipeTypes.BREWING;
    }

    @Override
    public boolean isEmpty() {
        return this.reagent.isEmpty();
    }

    @Override
    public ItemStack itematic$createIcon(RegistryEntryLookup<Item> items) {
        return new ItemStack(items.getOrThrow(ItemKeys.BREWING_STAND));
    }

    @Override
    public DefaultedList<Ingredient> itematic$ingredients(RegistryEntryLookup<Item> items) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(2);
        ingredients.add(this.reagent());
        ingredients.add(this.inputIngredient(items));
        return ingredients;
    }

    public Optional<ItemStack> reagentRemainder() {
        return this.reagent.itematic$remainder().map(ItemStack::copy);
    }

    protected RegistryEntry<T> base() {
        return this.base;
    }

    public Ingredient reagent() {
        return this.reagent;
    }

    protected RegistryEntry<T> result() {
        return this.result;
    }

    public abstract Ingredient inputIngredient(RegistryEntryLookup<Item> items);
    protected abstract boolean matches(ItemStack base);
    protected abstract ItemStack craft(ItemStack base);
}
