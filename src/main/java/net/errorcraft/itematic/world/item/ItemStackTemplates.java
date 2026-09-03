package net.errorcraft.itematic.world.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemStackTemplates {
    private ItemStackTemplates() {}

    public static ItemStackTemplate of(Holder<Item> item) {
        return new ItemStackTemplate(item, 1, DataComponentPatch.EMPTY);
    }

    public static ItemStackTemplate of(Holder<Item> item, int count) {
        return new ItemStackTemplate(item, count, DataComponentPatch.EMPTY);
    }

    public static ItemStackTemplate of(Holder<Item> item, DataComponentPatch patch) {
        return new ItemStackTemplate(item, 1, patch);
    }
}
