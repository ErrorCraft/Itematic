package net.errorcraft.itematic.assertion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockStateAssert {
    private final TestContext helper;
    private final BlockState state;

    BlockStateAssert(TestContext helper, BlockState state) {
        this.helper = Objects.requireNonNull(helper);
        this.state = Assert.isNotNull(this.helper, state, "block state");
    }

    public BlockStateAssert is(Block block) {
        if (this.state.isOf(block)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_type",
            "block",
            Registries.BLOCK.getId(block),
            Registries.BLOCK.getId(this.state.getBlock())
        );
    }

    public BlockStateAssert isNot(Block block) {
        if (!this.state.isOf(block)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.did_not_expect_type",
            "block",
            Registries.BLOCK.getId(block)
        );
    }

    public BlockStateAssert is(TagKey<Block> tag) {
        if (this.state.isIn(tag)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_tag",
            "block",
            tag.id()
        );
    }

    public <T extends Comparable<T>> BlockStateAssert hasProperty(Property<T> property, T expected) {
        if (expected.equals(this.state.get(property))) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_type",
            property.getName() + " block state property",
            expected,
            this.state.get(property)
        );
    }

    public <T extends Comparable<T>> BlockStateAssert hasProperty(Property<T> expected, T value, Supplier<String> message) {
        if (value.equals(this.state.get(expected))) {
            return this;
        }

        throw this.helper.createError(Text.literal(message.get()));
    }

    public BlockStateAssert hasProperty(IntProperty property, Consumer<IntsAssert> expectedAssertion) {
        expectedAssertion.accept(Assert.ints(this.helper, this.state.get(property), property.getName() + " block state property"));
        return this;
    }
}
