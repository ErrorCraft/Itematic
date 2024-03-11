package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.block.LightBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LevelModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        BlockStateComponent blockState = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        Integer level = blockState.getValue(LightBlock.LEVEL_15);
        if (level == null) {
            return 1.0f;
        }

        return level / 16.0f;
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.LIGHT);
    }
}
