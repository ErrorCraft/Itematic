package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ItemBase(ItemBaseDisplay display, int maxCount) {
    public static final Codec<ItemBase> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemBaseDisplay.CODEC.fieldOf("display").forGetter(ItemBase::display),
        Codec.intRange(ItemBase.MIN_MAX_COUNT, ItemBase.MAX_MAX_COUNT).fieldOf("max_count").forGetter(ItemBase::maxCount)
    ).apply(instance, ItemBase::new));
    public static final int MIN_MAX_COUNT = 1;
    public static final int MAX_MAX_COUNT = 64;

    public ItemBase(ItemBaseDisplay display) {
        this(display, MAX_MAX_COUNT);
    }
}
