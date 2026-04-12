package net.errorcraft.itematic.assertion;

import net.minecraft.test.TestContext;

import java.util.Objects;

public class FloatsAssert {
    private final TestContext helper;
    private final float value;
    private final String name;

    FloatsAssert(TestContext helper, float value, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public FloatsAssert equals(float expected) {
        if (this.value == expected) {
            return this;
        }

        throw this.helper.createError(
            "test.error.value_not_equal",
            this.name,
            expected,
            this.value
        );
    }

    public FloatsAssert isGreaterThan(float expected) {
        if (this.value > expected) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_value_greater_than",
            this.name,
            expected,
            this.value
        );
    }
}
