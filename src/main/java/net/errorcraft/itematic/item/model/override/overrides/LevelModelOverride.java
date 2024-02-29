package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LevelModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        NbtCompound blockStateNbt = stack.getSubNbt(BlockItem.BLOCK_STATE_TAG_KEY);
        if (blockStateNbt == null) {
            return 1.0f;
        }

        NbtElement levelNbt = blockStateNbt.get(LightBlock.LEVEL_15.getName());
        if (levelNbt == null) {
            return 1.0f;
        }

        try {
            return Integer.parseInt(levelNbt.asString()) / 16.0f;
        } catch (NumberFormatException ignored) {
            return 1.0f;
        }
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.itematic$isOf(ItemKeys.LIGHT);
    }
}
