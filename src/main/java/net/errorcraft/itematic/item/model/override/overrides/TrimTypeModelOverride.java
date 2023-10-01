package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.model.override.ClampedModelOverride;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrimTypeModelOverride implements ClampedModelOverride {
    @Override
    public float applyUnclamped(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        if (world == null) {
            return 0.0f;
        }
        return ArmorTrim.getTrim(world.getRegistryManager(), stack, true)
            .map(ArmorTrim::getMaterial)
            .map(RegistryEntry::value)
            .map(ArmorTrimMaterial::itemModelIndex)
            .orElse(0.0f);
    }

    @Override
    public boolean isApplicable(ItemStack stack) {
        return stack.isIn(ItemTags.TRIMMABLE_ARMOR);
    }
}
