package net.errorcraft.itematic.gametest;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.GameTestException;

import java.util.function.Consumer;

public class Assert {
    private Assert() {}

    public static void itemStackIsOf(ItemStack value, RegistryKey<Item> expected) {
        if (value == null) {
            throw new GameTestException("Expected item stack to be of " + expected + ", but the item stack was null");
        }
        if (!value.itematic$isOf(expected)) {
            throw new GameTestException("Expected item stack to be of " + expected + ", got " + value.itematic$key() + " instead");
        }
    }

    public static void itemStackIsEmpty(ItemStack value) {
        if (!value.isEmpty()) {
            throw new GameTestException("Expected item stack to be empty, got " + value + " instead");
        }
    }

    public static <T> void itemStackHasComponent(ItemStack value, DataComponentType<T> type) {
        TestUtil.getComponent(value, type);
    }

    public static <T> void itemStackHasComponent(ItemStack value, DataComponentType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getComponent(value, type));
    }

    public static void itemStackHasPotion(ItemStack value, RegistryEntry<Potion> expected) {
        itemStackHasComponent(value, DataComponentTypes.POTION_CONTENTS, component -> {
            RegistryEntry<Potion> potion = component.potion()
                .orElseThrow(() -> new GameTestException("Expected item stack to have potion " + expected.getKey().orElseThrow()));
            if (expected != potion) {
                throw new GameTestException("Expected item stack to have potion " + expected.getKey().orElseThrow() + ", got " + potion.getKey().orElseThrow() + " instead");
            }
        });
    }

    public static void areIntsEqual(int value, int expected) {
        areIntsEqual(value, expected, (v, e) -> "Expected value to be " + e + ", got " + v + " instead");
    }

    public static void areIntsEqual(int value, int expected, ComparingIntStringSupplier messageSupplier) {
        if (value != expected) {
            throw new GameTestException(messageSupplier.get(value, expected));
        }
    }

    public static void areFloatsEqual(float value, float expected, ComparingFloatStringSupplier messageSupplier) {
        if (value != expected) {
            throw new GameTestException(messageSupplier.get(value, expected));
        }
    }

    public static <T> void forAll(Iterable<T> elements, Consumer<T> elementAssertion) {
        for (T element : elements) {
            elementAssertion.accept(element);
        }
    }

    @FunctionalInterface
    public interface ComparingIntStringSupplier {
        String get(int value, int expected);
    }

    @FunctionalInterface
    public interface ComparingFloatStringSupplier {
        String get(float value, float expected);
    }
}
