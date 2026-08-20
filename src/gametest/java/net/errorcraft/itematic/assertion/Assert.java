package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Assert {
    private Assert() {}

    public static void isTrue(GameTestHelper helper, boolean condition, Supplier<String> message) {
        if (condition) {
            return;
        }

        throw helper.assertionException(Component.literal(message.get()));
    }

    public static void isFalse(GameTestHelper helper, boolean condition, Supplier<String> message) {
        if (!condition) {
            return;
        }

        throw helper.assertionException(Component.literal(message.get()));
    }

    public static <T> T isNotNull(GameTestHelper helper, @Nullable T object, String name) {
        if (object != null) {
            return object;
        }

        throw helper.assertionException(
            "test.error.expected_not_null",
            name
        );
    }

    public static <T> void areEqual(GameTestHelper helper, T value, T expected, String type) {
        if (Objects.equals(value, expected)) {
            return;
        }

        throw helper.assertionException(
            "test.error.expected_type",
            type,
            value,
            expected
        );
    }

    public static <T> void areEqual(GameTestHelper helper, T value, T expected, Supplier<String> message) {
        if (Objects.equals(value, expected)) {
            return;
        }

        throw helper.assertionException(Component.literal(message.get()));
    }

    public static <T, U extends T> void isInstance(GameTestHelper helper, T value, Class<U> expectedClass, Supplier<String> message, Consumer<U> assertion) {
        if (expectedClass.isInstance(value)) {
            assertion.accept(expectedClass.cast(value));
            return;
        }

        throw helper.assertionException(Component.literal(message.get()));
    }

    public static IntsAssert ints(GameTestHelper helper, int value, String name) {
        return new IntsAssert(helper, value, name);
    }

    public static FloatsAssert floats(GameTestHelper helper, float value, String name) {
        return new FloatsAssert(helper, value, name);
    }

    public static DoublesAssert doubles(GameTestHelper helper, double value, String name) {
        return new DoublesAssert(helper, value, name);
    }

    public static BlockStateAssert blockState(GameTestHelper helper, BlockPos pos) {
        BlockState state = helper.getLevel().getBlockState(helper.absolutePos(pos));
        return new BlockStateAssert(helper, state);
    }

    public static <T extends BlockEntity> void blockEntity(GameTestHelper helper, BlockPos pos, BlockEntityType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getBlockEntity(helper, pos, type));
    }

    public static FluidStateAssert fluidState(GameTestHelper helper, BlockPos pos) {
        FluidState state = helper.getLevel().getFluidState(helper.absolutePos(pos));
        return new FluidStateAssert(helper, state);
    }

    public static <E extends Entity> EntityTypeAssert<E> entityType(GameTestHelper helper, EntityType<E> type) {
        return new EntityTypeAssert<>(helper, type);
    }

    public static <E extends Entity> EntityAssert<E> entity(GameTestHelper helper, E entity) {
        return new EntityAssert<>(helper, entity);
    }

    public static <E extends LivingEntity> LivingEntityAssert<E> livingEntity(GameTestHelper helper, E entity) {
        return new LivingEntityAssert<>(helper, entity);
    }

    public static ItemEntityAssert itemEntity(GameTestHelper helper, ItemEntity entity) {
        return new ItemEntityAssert(helper, entity);
    }

    public static ItemStackAssert itemStack(GameTestHelper helper, ItemStack stack) {
        return new ItemStackAssert(helper, stack);
    }

    public static ItemStackAssert itemStack(GameTestHelper helper, ItemStack stack, String name) {
        return new ItemStackAssert(helper, stack, name);
    }
}
