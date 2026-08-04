package net.errorcraft.itematic.assertion;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockStateAssert {
    private final GameTestHelper helper;
    private final BlockState state;

    BlockStateAssert(GameTestHelper helper, BlockState state) {
        this.helper = Objects.requireNonNull(helper);
        this.state = Assert.isNotNull(this.helper, state, "block state");
    }

    public BlockStateAssert is(Block block) {
        if (this.state.is(block)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_type",
            "block",
            BuiltInRegistries.BLOCK.getKey(block),
            BuiltInRegistries.BLOCK.getKey(this.state.getBlock())
        );
    }

    public BlockStateAssert isNot(Block block) {
        if (!this.state.is(block)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.did_not_expect_type",
            "block",
            BuiltInRegistries.BLOCK.getKey(block)
        );
    }

    public BlockStateAssert is(TagKey<Block> tag) {
        if (this.state.is(tag)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_tag",
            "block",
            tag.location()
        );
    }

    public <T extends Comparable<T>> BlockStateAssert hasProperty(Property<T> property, T expected) {
        if (expected.equals(this.state.getValue(property))) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_type",
            property.getName() + " block state property",
            expected,
            this.state.getValue(property)
        );
    }

    public <T extends Comparable<T>> BlockStateAssert hasProperty(Property<T> expected, T value, Supplier<String> message) {
        if (value.equals(this.state.getValue(expected))) {
            return this;
        }

        throw this.helper.assertionException(Component.literal(message.get()));
    }

    public BlockStateAssert hasProperty(IntegerProperty property, Consumer<IntsAssert> expectedAssertion) {
        expectedAssertion.accept(Assert.ints(this.helper, this.state.getValue(property), property.getName() + " block state property"));
        return this;
    }
}
