package net.errorcraft.itematic.world.item.crafting.display;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ItematicSlotDisplays {
    public static final SlotDisplay.Type<PotionSlotDisplay> POTION = register(
        "potion",
        new SlotDisplay.Type<>(PotionSlotDisplay.CODEC, PotionSlotDisplay.STREAM_CODEC)
    );

    private ItematicSlotDisplays() {}

    public static void init() {}

    private static <T extends SlotDisplay> SlotDisplay.Type<T> register(String id, SlotDisplay.Type<T> display) {
        return Registry.register(BuiltInRegistries.SLOT_DISPLAY, id, display);
    }
}
