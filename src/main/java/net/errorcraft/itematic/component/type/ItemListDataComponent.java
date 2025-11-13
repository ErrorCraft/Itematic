package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

public record ItemListDataComponent(RegistryEntryList<Item> items) {
    public static final Codec<ItemListDataComponent> CODEC = RegistryCodecs.entryList(RegistryKeys.ITEM)
        .xmap(ItemListDataComponent::new, ItemListDataComponent::items);
    public static final PacketCodec<RegistryByteBuf, ItemListDataComponent> PACKET_CODEC = PacketCodecs.registryEntryList(RegistryKeys.ITEM)
        .xmap(ItemListDataComponent::new, ItemListDataComponent::items);
    public static final ItemListDataComponent DEFAULT = new ItemListDataComponent(RegistryEntryList.empty());

    public boolean isValidFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return this.items.contains(stack.getRegistryEntry());
    }
}
