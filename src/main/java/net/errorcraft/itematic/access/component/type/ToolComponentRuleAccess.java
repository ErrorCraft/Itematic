package net.errorcraft.itematic.access.component.type;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface ToolComponentRuleAccess {
    default boolean itematic$matches(ItemStack stack, BlockState state) {
        return false;
    }
}
