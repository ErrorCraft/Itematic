package net.errorcraft.itematic.recipe.display.slot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ItematicSlotDisplaySerializers {
    public static final SlotDisplay.Type<PotionSlotDisplay> POTION = register("potion", new SlotDisplay.Type<>(PotionSlotDisplay.CODEC, PotionSlotDisplay.STREAM_CODEC));

    private ItematicSlotDisplaySerializers() {}

    public static void init() {}

    private static <T extends SlotDisplay> SlotDisplay.Type<T> register(String id, SlotDisplay.Type<T> serializer) {
        return Registry.register(BuiltInRegistries.SLOT_DISPLAY, id, serializer);
    }
}
