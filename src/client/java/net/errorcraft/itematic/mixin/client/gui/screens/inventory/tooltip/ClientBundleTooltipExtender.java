package net.errorcraft.itematic.mixin.client.gui.screens.inventory.tooltip;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.access.client.gui.screens.inventory.tooltip.ClientBundleTooltipAccess;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientBundleTooltip.class)
public class ClientBundleTooltipExtender implements ClientBundleTooltipAccess {
    @Unique
    private static final ScopedValue<Fraction> CAPACITY = ScopedValue.newInstance();

    @Unique
    private Fraction capacity;

    @Unique
    private ItemHolderRules itemHolderRules;

    @WrapOperation(
        method = "renderImage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/BundleContents;weight()Lcom/mojang/serialization/DataResult;"
        )
    )
    private DataResult<Fraction> useItemHolderRulesDataComponent(BundleContents instance, Operation<DataResult<Fraction>> original) {
        return instance.itematic$occupancy(this.itemHolderRules);
    }

    @WrapOperation(
        method = "renderBundleWithItemsTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientBundleTooltip;drawProgressbar(IILnet/minecraft/client/gui/Font;Lnet/minecraft/client/gui/GuiGraphics;Lorg/apache/commons/lang3/math/Fraction;)V"
        )
    )
    private void passCapacity(int x, int y, Font font, GuiGraphics graphics, Fraction weight, Operation<Void> original) {
        ScopedValue.where(CAPACITY, this.capacity)
            .run(() -> original.call(x, y, font, graphics, weight));
    }

    @WrapOperation(
        method = "getProgressBarFillText",
        at = @At(
            value = "FIELD",
            target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Fraction capacityFromDataComponent(Operation<Fraction> original) {
        return CAPACITY.get();
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
