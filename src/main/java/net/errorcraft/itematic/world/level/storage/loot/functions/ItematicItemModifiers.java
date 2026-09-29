package net.errorcraft.itematic.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class ItematicItemModifiers {
    private ItematicItemModifiers() {}

    public static void bootstrap(Registry<MapCodec<? extends LootItemFunction>> registry) {
        Registry.register(registry, "split", SplitItemModifier.CODEC);
        Registry.register(registry, "set_item_pointer_location", SetItemPointerLocationItemModifier.CODEC);
    }
}
