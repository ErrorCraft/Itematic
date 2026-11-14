package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.BlockBrusher;
import net.minecraft.core.component.DataComponentMap;

public record BrushItemBehavior(BlockBrusher blockBrusher) implements ItemBehavior<BrushItemBehavior> {
    public static final Codec<BrushItemBehavior> CODEC = BlockBrusher.CODEC.xmap(
        BrushItemBehavior::new,
        BrushItemBehavior::blockBrusher
    );

    public static BrushItemBehavior of(BlockBrusher blockBrusher) {
        return new BrushItemBehavior(blockBrusher);
    }

    @Override
    public ItemBehaviorType<BrushItemBehavior> type() {
        return ItemBehaviorType.BRUSH;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.BLOCK_BRUSHER, this.blockBrusher);
    }
}
