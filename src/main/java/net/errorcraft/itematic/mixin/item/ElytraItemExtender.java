package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ElytraItem.class)
public class ElytraItemExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public static boolean isUsable(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.DAMAGEABLE)
            .map(c -> c.isUsable(stack))
            .orElse(false);
    }
}
