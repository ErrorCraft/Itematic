package net.errorcraft.itematic.loot.function;

import net.errorcraft.itematic.mixin.loot.function.LootFunctionTypesAccessor;
import net.minecraft.loot.function.LootFunctionType;

public class ItematicItemModifierTypes {
    public static final LootFunctionType<DyeItemModifier> DYE = LootFunctionTypesAccessor.register("dye", DyeItemModifier.CODEC);
    public static final LootFunctionType<SetRandomPotionItemModifier> SET_RANDOM_POTION = LootFunctionTypesAccessor.register("set_random_potion", SetRandomPotionItemModifier.CODEC);
    public static final LootFunctionType<SplitItemModifier> SPLIT = LootFunctionTypesAccessor.register("split", SplitItemModifier.CODEC);
    public static final LootFunctionType<SetItemPointerLocationItemModifier> SET_ITEM_POINTER_LOCATION = LootFunctionTypesAccessor.register("set_item_pointer_location", SetItemPointerLocationItemModifier.CODEC);

    private ItematicItemModifierTypes() {}

    public static void init() {}
}
