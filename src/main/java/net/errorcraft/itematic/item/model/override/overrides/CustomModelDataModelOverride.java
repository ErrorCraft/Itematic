package net.errorcraft.itematic.item.model.override.overrides;

import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CustomModelDataModelOverride implements ModelOverride {
    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable LivingEntity target, int seed) {
        return stack.getOrDefault(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelDataComponent.DEFAULT).value();
    }
}
