package net.errorcraft.itematic.mixin.world.inventory.tooltip;

import net.errorcraft.itematic.access.world.inventory.tooltip.BundleTooltipAccess;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BundleTooltip.class)
public class BundleTooltipExtender implements BundleTooltipAccess {
    @Unique
    private Fraction capacity;

    @Unique
    private ItemHolderRules itemHolderRules;

    @Override
    public Fraction itematic$capacity() {
        return this.capacity;
    }

    @Override
    public void itematic$setCapacity(Fraction capacity) {
        this.capacity = capacity;
    }

    @Override
    public ItemHolderRules itematic$itemHolderRules() {
        return this.itemHolderRules;
    }

    @Override
    public void itematic$setItemHolderRules(ItemHolderRules itemHolderRules) {
        this.itemHolderRules = itemHolderRules;
    }
}
