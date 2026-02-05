package net.errorcraft.itematic.mixin.component.type;

import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EquippableComponent.class)
public class EquippableComponentExtender {
    @Shadow
    @Final
    @Mutable
    private boolean dispensable;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/component/type/EquippableComponent;dispensable:Z",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void doNotUseDispensableField(EquippableComponent instance, boolean value) {
        this.dispensable = true;
    }

    @Redirect(
        method = "equip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }
}
