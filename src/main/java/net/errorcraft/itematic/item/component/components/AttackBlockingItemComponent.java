package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;

public record AttackBlockingItemComponent(BlocksAttacksComponent blocksAttacks) implements ItemComponent<AttackBlockingItemComponent> {
    public static final Codec<AttackBlockingItemComponent> CODEC = BlocksAttacksComponent.CODEC.xmap(
        AttackBlockingItemComponent::new,
        AttackBlockingItemComponent::blocksAttacks
    );

    public static AttackBlockingItemComponent of(BlocksAttacksComponent blocksAttacks) {
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
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.BLOCKS_ATTACKS, this.blocksAttacks);
    }
}
