package net.errorcraft.itematic.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.HashedPatchMap;
import net.minecraft.network.HashedStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record UnloadableHashedStack(ResourceKey<Item> item, int count, HashedPatchMap components) implements HashedStack {
    public static final StreamCodec<RegistryFriendlyByteBuf, UnloadableHashedStack> STREAM_CODEC = StreamCodec.composite(
        ResourceKey.streamCodec(Registries.ITEM), UnloadableHashedStack::item,
        ByteBufCodecs.VAR_INT, UnloadableHashedStack::count,
        HashedPatchMap.STREAM_CODEC, UnloadableHashedStack::components,
        UnloadableHashedStack::new
    );

    @Override
    public boolean matches(ItemStack stack, HashedPatchMap.HashGenerator hasher) {
        return this.count == stack.getCount()
            && this.item.equals(stack.itematic$key())
            && this.components.matches(stack.getComponentsPatch(), hasher);
    }
}
