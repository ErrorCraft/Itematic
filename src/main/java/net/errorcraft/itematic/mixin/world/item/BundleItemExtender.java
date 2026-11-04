package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BundleItem.class)
public class BundleItemExtender {
    @WrapMethod(
        method = "toggleSelectedItem"
    )
    private static void useItemHolderRulesDataComponent(ItemStack stack, int selectedItem, Operation<Void> original) {
        ItemHolderRules itemHolderRules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (itemHolderRules != null) {
            itemHolderRules.toggleItem(stack, selectedItem);
        }
    }
}
