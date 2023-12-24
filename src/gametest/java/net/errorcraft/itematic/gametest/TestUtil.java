package net.errorcraft.itematic.gametest;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.test.GameTestException;

public class TestUtil {
    private TestUtil() {}

    public static <T extends ItemComponent> T getItemComponent(ItemStack stack, ItemComponentType<T> type) {
        return stack.itematic$getComponent(type)
            .orElseThrow(() -> new GameTestException("Item " + stack.itematic$key() + " does not contain the " + ItematicRegistries.ITEM_COMPONENT_TYPE.getKey(type).orElseThrow() + " item component"));
    }
}
