package net.errorcraft.itematic.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record BrewingRecipeDisplay(SlotDisplay base, SlotDisplay reagent, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<BrewingRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SlotDisplay.CODEC.fieldOf("base").forGetter(BrewingRecipeDisplay::base),
        SlotDisplay.CODEC.fieldOf("reagent").forGetter(BrewingRecipeDisplay::reagent),
        SlotDisplay.CODEC.fieldOf("result").forGetter(BrewingRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(BrewingRecipeDisplay::craftingStation)
    ).apply(instance, BrewingRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::base,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::reagent,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::result,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::craftingStation,
        BrewingRecipeDisplay::new
    );

    @Override
    public Type<? extends RecipeDisplay> type() {
        return ItematicRecipeDisplays.BREWING;
    }
}
