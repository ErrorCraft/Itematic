package net.errorcraft.itematic.client.item;

import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public record ModelPredicateProviderWrapper(ModelOverride override) implements ModelPredicateProvider {
    @Override
    public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return this.override.apply(stack, world, entity, seed);
    }
}
