package net.errorcraft.itematic.client.resources.item.bar.progress.provider;

import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.client.resources.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

public class ItemHolderOccupancyProgressProvider implements ProgressProvider {
    @Override
    public boolean isVisible(ItemStack stack) {
        return occupancy(stack)
            .filter(occupancy -> occupancy.compareTo(Fraction.ZERO) > 0)
            .isPresent();
    }

    @Override
    public float get(ItemStack stack) {
        return occupancy(stack).map(Fraction::floatValue).orElse(0.0f);
    }

    private static Optional<Fraction> occupancy(ItemStack stack) {
        DataResult<Fraction> occupancy = ItemHolderItemBehavior.occupancy(stack);
        if (occupancy == null) {
            return Optional.empty();
        }

        return occupancy.result();
    }
}
