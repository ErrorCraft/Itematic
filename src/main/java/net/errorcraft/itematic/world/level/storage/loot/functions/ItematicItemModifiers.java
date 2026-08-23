package net.errorcraft.itematic.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class ItematicItemModifiers {
    private ItematicItemModifiers() {}

    public static void init() {
        register(
            "split",
            SplitItemModifier.CODEC
        );
        register(
            "set_item_pointer_location",
            SetItemPointerLocationItemModifier.CODEC
        );
    }

    private static <T extends LootItemFunction> void register(String id, MapCodec<T> codec) {
        Registry.register(
            BuiltInRegistries.LOOT_FUNCTION_TYPE,
            Identifier.withDefaultNamespace(id),
            codec
        );
    }
}
