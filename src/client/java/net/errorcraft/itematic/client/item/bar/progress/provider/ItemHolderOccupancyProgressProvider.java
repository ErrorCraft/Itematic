package net.errorcraft.itematic.client.item.bar.progress.provider;

import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProviderKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

public class ItemHolderOccupancyProgressProvider implements ProgressProvider {
    @Override
    public Identifier id() {
        return ProgressProviderKeys.ITEM_HOLDER_OCCUPANCY;
    }

    @Override
    public boolean isVisible(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            .getOccupancy()
            .compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public float get(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            .getOccupancy()
            .floatValue();
    }
}
