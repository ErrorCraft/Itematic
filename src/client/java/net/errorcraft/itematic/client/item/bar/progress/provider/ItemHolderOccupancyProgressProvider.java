package net.errorcraft.itematic.client.item.bar.progress.provider;

import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProviderKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

public class ItemHolderOccupancyProgressProvider implements ProgressProvider {
    @Override
    public Identifier id() {
        return ProgressProviderKeys.ITEM_HOLDER_OCCUPANCY;
    }

    @Override
    public boolean isVisible(ItemStack stack) {
        return occupancy(stack).filter(f -> f.compareTo(Fraction.ZERO) > 0).isPresent();
    }

    @Override
    public float get(ItemStack stack) {
        return occupancy(stack).map(Fraction::floatValue).orElse(0.0f);
    }

    private static Optional<Fraction> occupancy(ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.ITEM_HOLDER)
            .map(c -> c.occupancy(stack));
    }
}
