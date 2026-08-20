package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;

public class FloatsAssert {
    private final GameTestHelper helper;
    private final float value;
    private final String name;

    FloatsAssert(GameTestHelper helper, float value, String name) {
        this.helper = helper;
        this.value = value;
        this.name = name;
    }

    public FloatsAssert equals(float expected) {
        if (this.value == expected) {
            return this;
        }

        throw this.helper.assertionException(
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

        throw this.helper.assertionException(
            "test.error.expected_value_greater_than",
            this.name,
            expected,
            this.value
        );
    }
}
