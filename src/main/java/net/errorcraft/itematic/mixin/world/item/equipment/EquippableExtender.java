package net.errorcraft.itematic.mixin.world.item.equipment;

import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
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
