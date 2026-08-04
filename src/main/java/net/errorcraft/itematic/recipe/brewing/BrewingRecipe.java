package net.errorcraft.itematic.recipe.brewing;

import com.mojang.datafixers.util.Function5;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.Optional;

public abstract class BrewingRecipe<T> implements Recipe<BrewingRecipeInput>, RecipeAccess {
    public static final int DEFAULT_BREWING_TIME = 400;
    private final String group;
    private final Holder<T> base;
    private final Ingredient reagent;
    private final Holder<T> result;
    private final int brewingTime;

    protected BrewingRecipe(String group, Holder<T> base, Ingredient reagent, Holder<T> result, int brewingTime) {
        this.group = group;
        this.base = base;
        this.reagent = reagent;
        this.result = result;
        this.brewingTime = brewingTime;
    }

    protected static <T, R extends BrewingRecipe<T>> MapCodec<R> createCodec(ResourceKey<Registry<T>> registry, Function5<String, Holder<T>, Ingredient, Holder<T>, Integer, R> creator) {
        Codec<Holder<T>> entryCodec = RegistryFixedCodec.create(registry);
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(R::group),
            entryCodec.fieldOf("base").forGetter(R::base),
            Ingredient.CODEC.fieldOf("reagent").forGetter(R::reagent),
            entryCodec.fieldOf("result").forGetter(R::result),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("brewing_time", DEFAULT_BREWING_TIME).forGetter(R::brewingTime)
        ).apply(instance, creator));
    }

    protected static <T, R extends BrewingRecipe<T>> StreamCodec<RegistryFriendlyByteBuf, R> createPacketCodec(ResourceKey<Registry<T>> registry, Function5<String, Holder<T>, Ingredient, Holder<T>, Integer, R> creator) {
        StreamCodec<RegistryFriendlyByteBuf, Holder<T>> entryPacketCodec = ByteBufCodecs.holderRegistry(registry);
        return StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, R::group,
            entryPacketCodec, R::base,
            Ingredient.CONTENTS_STREAM_CODEC, R::reagent,
            entryPacketCodec, R::result,
            ByteBufCodecs.VAR_INT, R::brewingTime,
            creator
        );
    }

    @Override
    public boolean matches(BrewingRecipeInput input, Level world) {
        return this.reagent.test(input.reagent()) && this.matches(input.base());
    }

    @Override
    public ItemStack assemble(BrewingRecipeInput input, HolderLookup.Provider registries) {
        return this.assemble(input.base());
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public RecipeType<? extends Recipe<BrewingRecipeInput>> getType() {
        return ItematicRecipeTypes.BREWING;
    }

    public Optional<ItemStack> reagentRemainder() {
        return this.reagent.itematic$remainder().map(ItemStack::copy);
    }

    protected Holder<T> base() {
        return this.base;
    }

    protected Ingredient reagent() {
        return this.reagent;
    }

    protected Holder<T> result() {
        return this.result;
    }

    public int brewingTime() {
        return this.brewingTime;
    }

    protected abstract boolean matches(ItemStack base);
    protected abstract ItemStack assemble(ItemStack base);
}
