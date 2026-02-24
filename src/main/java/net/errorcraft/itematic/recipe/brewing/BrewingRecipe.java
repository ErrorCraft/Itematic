package net.errorcraft.itematic.recipe.brewing;

import com.mojang.datafixers.util.Function4;
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
import net.minecraft.world.World;

import java.util.Optional;

public abstract class BrewingRecipe<T> implements Recipe<BrewingRecipeInput>, RecipeAccess {
    private final String group;
    private final RegistryEntry<T> base;
    private final Ingredient addition;
    private final RegistryEntry<T> result;

    protected BrewingRecipe(String group, RegistryEntry<T> base, Ingredient addition, RegistryEntry<T> result) {
        this.group = group;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    protected static <T, R extends BrewingRecipe<T>> MapCodec<R> createCodec(RegistryKey<Registry<T>> registry, Function4<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, R> creator) {
        Codec<RegistryEntry<T>> entryCodec = RegistryFixedCodec.of(registry);
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(R::getGroup),
            entryCodec.fieldOf("base").forGetter(R::base),
            Ingredient.CODEC.fieldOf("addition").forGetter(R::addition),
            entryCodec.fieldOf("result").forGetter(R::result)
        ).apply(instance, creator));
    }

    protected static <T, R extends BrewingRecipe<T>> PacketCodec<RegistryByteBuf, R> createPacketCodec(RegistryKey<Registry<T>> registry, Function4<String, RegistryEntry<T>, Ingredient, RegistryEntry<T>, R> creator) {
        PacketCodec<RegistryByteBuf, RegistryEntry<T>> entryPacketCodec = PacketCodecs.registryEntry(registry);
        return PacketCodec.tuple(
            PacketCodecs.STRING, R::getGroup,
            entryPacketCodec, R::base,
            Ingredient.PACKET_CODEC, R::addition,
            entryPacketCodec, R::result,
            creator
        );
    }

    @Override
    public boolean matches(BrewingRecipeInput input, World world) {
        return this.addition.test(input.addition()) && this.matches(input.base());
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

    public Optional<ItemStack> additionRemainder() {
        return this.addition.itematic$remainder().map(ItemStack::copy);
    }

    protected RegistryEntry<T> base() {
        return this.base;
    }

    protected Ingredient addition() {
        return this.addition;
    }

    protected RegistryEntry<T> result() {
        return this.result;
    }

    protected abstract boolean matches(ItemStack base);
    protected abstract ItemStack craft(ItemStack base);
}
