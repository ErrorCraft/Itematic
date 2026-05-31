package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Assert {
    private Assert() {}

    public static void isTrue(TestContext helper, boolean condition, Supplier<String> message) {
        if (!condition) {
            throw helper.createError(Text.literal(message.get()));
        }
    }

    public static void isFalse(TestContext helper, boolean condition, Supplier<String> message) {
        isTrue(helper, !condition, message);
    }

    public static <T> T isNotNull(TestContext helper, T object, String name) {
        if (object == null) {
            throw helper.createError(
                "test.error.expected_not_null",
                name
            );
        }

        return object;
    }

    public static <T> void areEqual(TestContext helper, T value, T expected, String type) {
        if (Objects.equals(value, expected)) {
            return;
        }

        throw helper.createError(
            "test.error.expected_type",
            type,
            value,
            expected
        );
    }

    public static <T, U extends T> void isInstance(TestContext helper, T value, Class<U> expectedClass, Supplier<String> message, Consumer<U> assertion) {
        if (expectedClass.isInstance(value)) {
            assertion.accept(expectedClass.cast(value));
            return;
        }

        throw helper.createError(Text.literal(message.get()));
    }

    public static IntsAssert ints(TestContext helper, int value, String name) {
        return new IntsAssert(helper, value, name);
    }

    public static FloatsAssert floats(TestContext helper, float value, String name) {
        return new FloatsAssert(helper, value, name);
    }

    public static DoublesAssert doubles(TestContext helper, double value, String name) {
        return new DoublesAssert(helper, value, name);
    }

    public static BlockStateAssert blockState(TestContext helper, BlockPos pos) {
        BlockState state = helper.getWorld().getBlockState(helper.getAbsolutePos(pos));
        return new BlockStateAssert(helper, state);
    }

    public static <T extends BlockEntity> void blockEntity(TestContext helper, BlockPos pos, BlockEntityType<T> type, Consumer<T> assertion) {
        assertion.accept(TestUtil.getBlockEntity(helper, pos, type));
    }

    public static FluidStateAssert fluidState(TestContext helper, BlockPos pos) {
        FluidState state = helper.getWorld().getFluidState(helper.getAbsolutePos(pos));
        return new FluidStateAssert(helper, state);
    }

    public static <E extends Entity> EntityTypeAssert<E> entityType(TestContext helper, EntityType<E> type) {
        return new EntityTypeAssert<>(helper, type);
    }

    public static <E extends Entity> EntityAssert<E> entity(TestContext helper, E entity) {
        return new EntityAssert<>(helper, entity);
    }

    public static <E extends LivingEntity> LivingEntityAssert<E> livingEntity(TestContext helper, E entity) {
        return new LivingEntityAssert<>(helper, entity);
    }

    public static ItemStackAssert itemStack(TestContext helper, ItemStack stack) {
        return new ItemStackAssert(helper, stack);
    }

    public static ItemStackAssert itemStack(TestContext helper, ItemStack stack, String name) {
        return new ItemStackAssert(helper, stack, name);
    }
}
