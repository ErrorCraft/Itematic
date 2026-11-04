package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;

public record BlockTransformerItemBehavior(Holder<BlockTransformer> blockTransformer) implements ItemBehavior<BlockTransformerItemBehavior> {
    public static final Codec<BlockTransformerItemBehavior> CODEC = BlockTransformer.CODEC.xmap(
        BlockTransformerItemBehavior::new,
        BlockTransformerItemBehavior::blockTransformer
    );

    public static BlockTransformerItemBehavior of(Holder<BlockTransformer> blockTransformer) {
        return new BlockTransformerItemBehavior(blockTransformer);
    }

    @Override
    public ItemBehaviorType<BlockTransformerItemBehavior> type() {
        return ItemBehaviorType.BLOCK_TRANSFORMER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BLOCK_TRANSFORMER, this.blockTransformer);
    }
}
