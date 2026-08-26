package net.errorcraft.itematic.world.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemStackTemplates {
    public static final MapCodec<ItemStackTemplate> SINGLE_ITEM_MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Item.CODEC.fieldOf(ItemInstance.FIELD_ID).forGetter(ItemStackTemplate::item),
        DataComponentPatch.CODEC.optionalFieldOf(ItemInstance.FIELD_COMPONENTS, DataComponentPatch.EMPTY).forGetter(ItemStackTemplate::components)
    ).apply(instance, ItemStackTemplates::of));

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
