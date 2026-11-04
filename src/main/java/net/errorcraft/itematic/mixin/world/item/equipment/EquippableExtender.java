package net.errorcraft.itematic.mixin.world.item.equipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.behaviors.EquipmentItemBehavior;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Equippable.class)
public class EquippableExtender {
    @Shadow
    @Final
    @Mutable
    private boolean dispensable;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/equipment/Equippable;dispensable:Z",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void doNotUseDispensableField(Equippable instance, boolean value) {
        this.dispensable = true;
    }

    @ModifyReturnValue(
        method = "swapWithEquipmentSlot",
        at = @At("RETURN")
    )
    private InteractionResult invokeEquipItemEventAndExchangeResultStack(InteractionResult original, ItemStack inHand) {
        ActionContext context = EquipmentItemBehavior.ON_EQUIP_ACTION_CONTEXT.get();
        if (original.consumesAction()) {
            inHand.itematic$invokeEvent(ItemEvent.EQUIP_ITEM, context);
        }

        return original.itematic$exchanged(context::exchangeStack);
    }

    @Redirect(
        method = "swapWithEquipmentSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, ItemStack inHand) {
        return instance.itematic$get(inHand.typeHolder());
    }
}
