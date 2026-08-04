package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemListDataComponent(HolderSet<Item> items) {
    public static final Codec<ItemListDataComponent> CODEC = RegistryCodecs.homogeneousList(Registries.ITEM)
        .xmap(ItemListDataComponent::new, ItemListDataComponent::items);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemListDataComponent> PACKET_CODEC = ByteBufCodecs.holderSet(Registries.ITEM)
        .map(ItemListDataComponent::new, ItemListDataComponent::items);
    public static final ItemListDataComponent DEFAULT = new ItemListDataComponent(HolderSet.empty());

    public boolean isValidFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return this.items.contains(stack.getItemHolder());
    }
}
