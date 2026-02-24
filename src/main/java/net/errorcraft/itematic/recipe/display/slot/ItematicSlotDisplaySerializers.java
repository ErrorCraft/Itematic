package net.errorcraft.itematic.recipe.display.slot;

import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItematicSlotDisplaySerializers {
    public static final SlotDisplay.Serializer<PotionSlotDisplay> POTION = register("potion", new SlotDisplay.Serializer<>(PotionSlotDisplay.CODEC, PotionSlotDisplay.PACKET_CODEC));

    private ItematicSlotDisplaySerializers() {}

    public static void init() {}

    private static <T extends SlotDisplay> SlotDisplay.Serializer<T> register(String id, SlotDisplay.Serializer<T> serializer) {
        return Registry.register(Registries.SLOT_DISPLAY, id, serializer);
    }
}
