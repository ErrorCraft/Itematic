package net.errorcraft.itematic.world.item.holder;

import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

public class BundleContentsUtil {
    private BundleContentsUtil() {}

    @Nullable
    public static DataResult<Fraction> occupancy(ItemInstance item) {
        BundleContents bundleContents = item.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return null;
        }

        Fraction capacity = item.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = item.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        return bundleContents.itematic$occupancy(rules)
            .map(fraction -> fraction.divideBy(capacity));
    }
}
