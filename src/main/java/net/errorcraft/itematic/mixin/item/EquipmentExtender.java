package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Equipment.class)
public interface EquipmentExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    @Nullable
    static Equipment fromStack(ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.EQUIPMENT).orElse(null);
    }

    @Redirect(
        method = "equipAndSwap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<T> doNotGetOrCreateStat(StatType<T> instance, T key) {
        return null;
    }
}
