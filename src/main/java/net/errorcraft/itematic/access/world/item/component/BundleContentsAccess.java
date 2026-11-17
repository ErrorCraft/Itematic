package net.errorcraft.itematic.access.world.item.component;

import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

public interface BundleContentsAccess {
    default Optional<TooltipComponent> itematic$tooltipImage(ItemStack stack) {
        return Optional.empty();
    }
    default void itematic$onDestroyed(ItemEntity item) {}
    default DataResult<Fraction> itematic$occupancy(ItemHolderRules rules) {
        throw new AssertionError("Implemented via mixin");
    }
    default BundleContents.Mutable itematic$asMutable(Fraction capacity, ItemHolderRules rules) {
        throw new AssertionError("Implemented via mixin");
    }
    interface MutableAccess {
        default void itematic$setFields(BundleContents bundleContents, Fraction capacity, ItemHolderRules rules) {}
    }
}
