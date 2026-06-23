package net.errorcraft.itematic.assertion;

import net.minecraft.test.TestContext;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;
import java.util.function.Consumer;

public class DoublesAssert {
    private final TestContext helper;
    private final double value;
    private final String name;

    DoublesAssert(TestContext helper, double value, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public DoublesAssert congruent(double modulus, Consumer<DoublesAssert> congruentAssertion) {
        if (modulus == 0.0d) {
            throw this.helper.createError("test.error.invalid_modulus");
        }

        congruentAssertion.accept(Assert.doubles(
            this.helper,
            MathHelper.floorMod(this.value, modulus),
            this.name + " (congruent to " + modulus + ")"
        ));
        return this;
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
