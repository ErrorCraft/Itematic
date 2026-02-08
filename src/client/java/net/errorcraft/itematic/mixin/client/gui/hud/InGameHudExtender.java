package net.errorcraft.itematic.mixin.client.gui.hud;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudExtender {
    @WrapOperation(
        method = "renderMiscOverlays",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        )
    )
    private Object checkPresenceEquipmentBehavior(ItemStack instance, ComponentType<EquippableComponent> type, Operation<Object> original) {
        if (!instance.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            return null;
        }

        return original.call(instance, type);
    }
}
