package net.errorcraft.itematic.client.item.bar.progress.provider;

import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProviderKeys;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

import java.util.Objects;

public class ItemHolderOccupancyProgressProvider implements ProgressProvider {
    @Override
    public Identifier id() {
        return ProgressProviderKeys.ITEM_HOLDER_OCCUPANCY;
    }

    @Override
    public boolean isVisible(ItemStack stack) {
        if (!stack.contains(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY)) {
            return false;
        }
        return stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            .getOccupancy()
            .compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public float get(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            .getOccupancy()
            .divideBy(Objects.requireNonNull(stack.get(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY)))
            .floatValue();
    }
}
