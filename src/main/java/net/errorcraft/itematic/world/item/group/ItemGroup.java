package net.errorcraft.itematic.world.item.group;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStackTemplate;

public record ItemGroup(Component name, ItemStackTemplate icon, HolderSet<ItemGroupEntryProvider> entries) {
    public static final Codec<ItemGroup> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ComponentSerialization.CODEC.fieldOf("name").forGetter(ItemGroup::name),
        ItemStackTemplate.CODEC.fieldOf("icon").forGetter(ItemGroup::icon),
        ItemGroupEntryProvider.LIST_CODEC.fieldOf("entries").forGetter(ItemGroup::entries)
    ).apply(instance, ItemGroup::new));
    public static final int DISPLAY_ON_EVERY_PAGE = -1;
}
