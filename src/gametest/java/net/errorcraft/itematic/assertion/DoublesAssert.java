package net.errorcraft.itematic.assertion;

import net.minecraft.test.TestContext;

import java.util.Objects;

public class DoublesAssert {
    private final TestContext helper;
    private final double value;
    private final String name;

    DoublesAssert(TestContext helper, double value, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public DoublesAssert equals(double expected) {
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
}
