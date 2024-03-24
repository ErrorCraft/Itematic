package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ItemBase(ItemBaseDisplay display) {
    public static final Codec<ItemBase> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemBaseDisplay.CODEC.fieldOf("display").forGetter(ItemBase::display)
    ).apply(instance, ItemBase::new));
    public static final int MAX_MAX_COUNT = 64;
}
