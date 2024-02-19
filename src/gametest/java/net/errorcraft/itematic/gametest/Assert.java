package net.errorcraft.itematic.gametest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.GameTestException;

import java.util.function.Consumer;

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

    public static void itemStackHasPotion(ItemStack value, RegistryEntry<Potion> expected) {
        RegistryEntry<Potion> valuePotion = PotionUtil.getPotion(value);
        if (expected != valuePotion) {
            throw new GameTestException("Expected item stack to have potion " + expected.getKey().orElseThrow() + ", got " + valuePotion.getKey().orElseThrow() + " instead");
        }
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
