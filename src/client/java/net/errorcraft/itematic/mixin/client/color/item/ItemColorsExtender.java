package net.errorcraft.itematic.mixin.client.color.item;

import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemColors.class)
public class ItemColorsExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getColor(ItemStack item, int tintIndex) {
        return item.itematic$getBehavior(ItemComponentTypes.TINTED)
            .map(TintedItemComponent::tint)
            .map(c -> c.color(item, tintIndex))
            .orElse(ItemColor.DEFAULT_COLOR);
    }
}
