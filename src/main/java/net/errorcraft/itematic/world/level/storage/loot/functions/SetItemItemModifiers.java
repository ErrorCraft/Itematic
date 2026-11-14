package net.errorcraft.itematic.world.level.storage.loot.functions;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemFunction;

import java.util.Optional;

public class SetItemItemModifiers {
    private SetItemItemModifiers() {}

    public static LootItemFunction.Builder builder(Holder<Item> item) {
        return () -> new SetItemFunction(Optional.empty(), item);
    }
}
