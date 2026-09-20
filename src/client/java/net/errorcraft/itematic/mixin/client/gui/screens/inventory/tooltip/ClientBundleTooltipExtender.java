package net.errorcraft.itematic.mixin.client.gui.screens.inventory.tooltip;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.access.client.gui.screens.inventory.tooltip.ClientBundleTooltipAccess;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipExtender implements ClientBundleTooltipAccess {
    @Unique
    private Fraction capacity;

    @Unique
    private ItemHolderRules itemHolderRules;

    @WrapOperation(
        method = "extractImage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/BundleContents;weight()Lcom/mojang/serialization/DataResult;"
        )
    )
    private DataResult<Fraction> useNormalizedOccupancyFromItemHolderRulesDataComponent(BundleContents instance, Operation<DataResult<Fraction>> original) {
        return instance.itematic$occupancy(this.itemHolderRules)
            .map(occupancy -> occupancy.divideBy(this.capacity));
    }

    @Override
    public void itematic$setCapacity(Fraction capacity) {
        this.capacity = capacity;
    }

    @Override
    public void itematic$setItemHolderRules(ItemHolderRules itemHolderRules) {
        this.itemHolderRules = itemHolderRules;
    }
}
