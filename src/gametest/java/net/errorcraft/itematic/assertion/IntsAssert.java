package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;

public class IntsAssert {
    private final GameTestHelper helper;
    private final int value;
    private final String name;

    IntsAssert(GameTestHelper helper, int value, String name) {
        this.helper = helper;
        this.value = value;
        this.name = name;
    }

    public IntsAssert equals(int expected) {
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
}
