package net.errorcraft.itematic.item.group.entry;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.util.StringIdentifiable;

public enum ItemGroupEntryType implements StringIdentifiable {
    STACK("stack", StackItemGroupEntry.CODEC),
    TAG("tag", TagItemGroupEntry.CODEC);

    private final String name;
    private final Codec<? extends ItemGroupEntry> codec;

    ItemGroupEntryType(String name, Codec<? extends ItemGroupEntry> codec) {
        this.name = name;
        this.codec = codec;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Codec<? extends ItemGroupEntry> codec() {
        return this.codec;
    }
}
