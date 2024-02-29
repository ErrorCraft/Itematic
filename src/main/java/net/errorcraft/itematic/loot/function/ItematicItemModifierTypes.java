package net.errorcraft.itematic.loot.function;

import net.errorcraft.itematic.mixin.loot.function.LootFunctionTypesAccessor;
import net.minecraft.loot.function.LootFunctionType;

public class ItematicItemModifierTypes {
    public static final LootFunctionType DYE = LootFunctionTypesAccessor.register("dye", DyeItemModifier.CODEC);

    private ItematicItemModifierTypes() {}

    public static void init() {}
}
