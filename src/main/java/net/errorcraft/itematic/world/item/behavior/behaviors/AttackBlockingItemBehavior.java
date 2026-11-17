package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlocksAttacks;

public record AttackBlockingItemBehavior(BlocksAttacks blocksAttacks) implements ItemBehavior {
    public static final Codec<AttackBlockingItemBehavior> CODEC = BlocksAttacks.CODEC.xmap(
        AttackBlockingItemBehavior::new,
        AttackBlockingItemBehavior::blocksAttacks
    );

    public static AttackBlockingItemBehavior of(BlocksAttacks blocksAttacks) {
        return new AttackBlockingItemBehavior(blocksAttacks);
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BLOCKS_ATTACKS, this.blocksAttacks);
    }
}
