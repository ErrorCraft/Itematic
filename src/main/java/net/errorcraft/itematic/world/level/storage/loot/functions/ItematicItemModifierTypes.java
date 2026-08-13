package net.errorcraft.itematic.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ItematicItemModifierTypes {
    public static final LootItemFunctionType<DyeItemModifier> DYE = register(
        "dye",
        DyeItemModifier.CODEC
    );
    public static final LootItemFunctionType<SetRandomPotionItemModifier> SET_RANDOM_POTION = register(
        "set_random_potion",
        SetRandomPotionItemModifier.CODEC
    );
    public static final LootItemFunctionType<SplitItemModifier> SPLIT = register(
        "split",
        SplitItemModifier.CODEC
    );
    public static final LootItemFunctionType<SetItemPointerLocationItemModifier> SET_ITEM_POINTER_LOCATION = register(
        "set_item_pointer_location",
        SetItemPointerLocationItemModifier.CODEC
    );

    private ItematicItemModifierTypes() {}

    public static void init() {}

    private static <T extends LootItemFunction> LootItemFunctionType<T> register(String id, MapCodec<T> codec) {
        return Registry.register(
            BuiltInRegistries.LOOT_FUNCTION_TYPE,
            Identifier.withDefaultNamespace(id),
            new LootItemFunctionType<>(codec)
        );
    }
}
