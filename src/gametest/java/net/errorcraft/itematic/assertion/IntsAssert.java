package net.errorcraft.itematic.assertion;

import net.minecraft.test.TestContext;

import java.util.Objects;

public class IntsAssert {
    private final TestContext helper;
    private final int value;
    private final String name;

    IntsAssert(TestContext helper, int value, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public IntsAssert equals(int expected) {
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
