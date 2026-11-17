package net.errorcraft.itematic.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.gui.BundleMouseActions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BundleMouseActions.class)
public class BundleMouseActionsExtender {
    @WrapOperation(
        method = "matches",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private boolean isBundlesCheckItemHolderRulesDataComponent(ItemStack instance, TagKey<Item> tagKey, Operation<Boolean> original) {
        return instance.has(ItematicDataComponents.ITEM_HOLDER_RULES);
    }
}
