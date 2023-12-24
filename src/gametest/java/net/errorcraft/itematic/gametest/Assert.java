package net.errorcraft.itematic.gametest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.test.GameTestException;

public class Assert {
    private Assert() {}

    public static void itemStackIsOf(ItemStack value, RegistryKey<Item> expected) {
        if (!value.itematic$isOf(expected)) {
            throw new GameTestException("Expected item stack to be of " + expected + ", got " + value.itematic$key() + " instead");
        }
    }

    public static void itemStackIsEmpty(ItemStack value) {
        if (!value.isEmpty()) {
            throw new GameTestException("Expected item stack to be empty, got " + value + " instead");
        }
    }
}
