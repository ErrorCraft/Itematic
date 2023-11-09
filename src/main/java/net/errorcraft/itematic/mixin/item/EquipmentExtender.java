package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
}
