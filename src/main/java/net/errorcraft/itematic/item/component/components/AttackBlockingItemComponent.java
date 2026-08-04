package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlocksAttacks;

public record AttackBlockingItemComponent(BlocksAttacks blocksAttacks) implements ItemComponent<AttackBlockingItemComponent> {
    public static final Codec<AttackBlockingItemComponent> CODEC = BlocksAttacks.CODEC.xmap(
        AttackBlockingItemComponent::new,
        AttackBlockingItemComponent::blocksAttacks
    );

    public static AttackBlockingItemComponent of(BlocksAttacks blocksAttacks) {
        return new AttackBlockingItemComponent(blocksAttacks);
    }

    @Override
    public ItemComponentType<AttackBlockingItemComponent> type() {
        return ItemComponentTypes.ATTACK_BLOCKING;
    }

    @Override
    public Codec<AttackBlockingItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BLOCKS_ATTACKS, this.blocksAttacks);
    }
}
