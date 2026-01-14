package net.errorcraft.itematic.gametest;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class Assert {
    private Assert() {}

    public static void fluidIsOf(TestContext context, Fluid fluid, BlockPos pos) {
        FluidState state = context.getWorld().getFluidState(context.getAbsolutePos(pos));
        if (!state.isOf(fluid)) {
            throw new GameTestException("Expected fluid to be of " + Registries.FLUID.getId(fluid) + ", got " + Registries.FLUID.getId(state.getFluid()) + " instead");
        }
    }

    public static void fluidIsIn(TestContext context, TagKey<Fluid> fluid, BlockPos pos) {
        FluidState state = context.getWorld().getFluidState(context.getAbsolutePos(pos));
        if (!state.isIn(fluid)) {
            throw new GameTestException("Expected fluid to be in " + fluid.id() + ", got " + Registries.FLUID.getId(state.getFluid()) + " instead");
        }
    }

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

    public static void itemStackIsNotEmpty(ItemStack value) {
        if (value.isEmpty()) {
            throw new GameTestException("Expected item stack not to be empty");
        }
    }

    public static <T> void itemStackHasDataComponent(ItemStack value, ComponentType<T> type) {
        if (value.get(type) == null) {
            throw new GameTestException("Expected item stack to contain the " + type + " component");
        }
    }

    public static <T> void itemStackDoesNotHaveDataComponent(ItemStack value, ComponentType<T> type) {
        if (value.get(type) != null) {
            throw new GameTestException("Expected item stack to not contain the " + type + " component");
        }
    }

    public static <T> void itemStackHasDataComponent(ItemStack value, ComponentType<T> type, Consumer<T> assertion) {
        itemStackHasDataComponent(value, type);
        assertion.accept(TestUtil.getDataComponent(value, type));
    }

    public static void itemStackHasPotion(ItemStack value, RegistryEntry<Potion> expected) {
        itemStackHasDataComponent(value, DataComponentTypes.POTION_CONTENTS, component -> {
            RegistryEntry<Potion> potion = component.potion()
                .orElseThrow(() -> new GameTestException("Expected item stack to have potion " + expected.getKey().orElseThrow()));
            if (expected != potion) {
                throw new GameTestException("Expected item stack to have potion " + expected.getKey().orElseThrow() + ", got " + potion.getKey().orElseThrow() + " instead");
            }
        });
    }

    public static void entityDoesNotHaveStatusEffect(LivingEntity entity, RegistryEntry<StatusEffect> effect) {
        if (entity.getStatusEffect(effect) != null) {
            throw new GameTestException("Expected entity to not have the " + effect.getKey().orElseThrow() + " status effect");
        }
    }

    public static <T extends BlockEntity> void blockEntityExists(TestContext context, BlockPos pos, BlockEntityType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getBlockEntity(context, pos, type));
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

    public static void areDoublesEqual(double value, double expected, ComparingDoubleStringSupplier messageSupplier) {
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

    @FunctionalInterface
    public interface ComparingDoubleStringSupplier {
        String get(double value, double expected);
    }
}
