package net.errorcraft.itematic.mixin.client.gui.hud;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class InGameHudExtender {
    @WrapOperation(
        method = "renderCameraOverlays",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    private Object checkPresenceEquipmentBehavior(ItemStack instance, DataComponentType<Equippable> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            return null;
        }

        return original.call(instance, type);
    }
}
