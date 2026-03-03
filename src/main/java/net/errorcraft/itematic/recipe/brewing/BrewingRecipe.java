package net.errorcraft.itematic.recipe.brewing;

import com.mojang.datafixers.util.Function5;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.recipe.ItematicRecipeTypes;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class BrewingRecipe<T> implements Recipe<BrewingRecipeInput>, RecipeAccess {
    public static final int DEFAULT_BREWING_TIME = 400;
    private final String group;
    private final RegistryEntry<T> base;
    private final Ingredient reagent;
    private final RegistryEntry<T> result;
    private final int brewingTime;

    protected BrewingRecipe(String group, RegistryEntry<T> base, Ingredient reagent, RegistryEntry<T> result, int brewingTime) {
        this.group = group;
        this.base = base;
        this.reagent = reagent;
        this.result = result;
        this.brewingTime = brewingTime;
    }

    protected static <T, R extends BrewingRecipe<T>> MapCodec<R> createCodec(RegistryKey<Registry<T>> registry, Function5<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, Integer, R> creator) {
        Codec<RegistryEntry<T>> entryCodec = RegistryFixedCodec.of(registry);
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(R::getGroup),
            entryCodec.fieldOf("base").forGetter(R::base),
            Ingredient.CODEC.fieldOf("reagent").forGetter(R::reagent),
            entryCodec.fieldOf("result").forGetter(R::result),
            Codecs.POSITIVE_INT.optionalFieldOf("brewing_time", DEFAULT_BREWING_TIME).forGetter(R::brewingTime)
        ).apply(instance, creator));
    }

    protected static <T, R extends BrewingRecipe<T>> PacketCodec<RegistryByteBuf, R> createPacketCodec(RegistryKey<Registry<T>> registry, Function5<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, Integer, R> creator) {
        PacketCodec<RegistryByteBuf, RegistryEntry<T>> entryPacketCodec = PacketCodecs.registryEntry(registry);
        return PacketCodec.tuple(
            PacketCodecs.STRING, R::getGroup,
            entryPacketCodec, R::base,
            Ingredient.PACKET_CODEC, R::reagent,
            entryPacketCodec, R::result,
            PacketCodecs.VAR_INT, R::brewingTime,
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
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeType<? extends Recipe<BrewingRecipeInput>> getType() {
        return ItematicRecipeTypes.BREWING;
    }

    public Optional<ItemStack> reagentRemainder() {
        return this.reagent.itematic$remainder().map(ItemStack::copy);
    }

    protected RegistryEntry<T> base() {
        return this.base;
    }

    protected Ingredient reagent() {
        return this.reagent;
    }

    protected RegistryEntry<T> result() {
        return this.result;
    }

    public int brewingTime() {
        return this.brewingTime;
    }

    protected abstract boolean matches(ItemStack base);
    protected abstract ItemStack craft(ItemStack base);
}
