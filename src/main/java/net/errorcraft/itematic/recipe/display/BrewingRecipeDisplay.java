package net.errorcraft.itematic.recipe.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;

public record BrewingRecipeDisplay(SlotDisplay base, SlotDisplay addition, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<BrewingRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SlotDisplay.CODEC.fieldOf("base").forGetter(BrewingRecipeDisplay::base),
        SlotDisplay.CODEC.fieldOf("addition").forGetter(BrewingRecipeDisplay::addition),
        SlotDisplay.CODEC.fieldOf("result").forGetter(BrewingRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(BrewingRecipeDisplay::craftingStation)
    ).apply(instance, BrewingRecipeDisplay::new));
    public static final PacketCodec<RegistryByteBuf, BrewingRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::base,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::addition,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::result,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::craftingStation,
        BrewingRecipeDisplay::new
    );

    @Override
    public Serializer<? extends RecipeDisplay> serializer() {
        return ItematicRecipeDisplaySerializers.BREWING;
    }
}
