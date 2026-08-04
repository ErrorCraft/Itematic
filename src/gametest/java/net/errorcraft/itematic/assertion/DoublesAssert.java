package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.Mth;

import java.util.Objects;
import java.util.function.Consumer;

public class DoublesAssert {
    private final GameTestHelper helper;
    private final double value;
    private final String name;

    DoublesAssert(GameTestHelper helper, double value, String name) {
        this.helper = Objects.requireNonNull(helper);
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public DoublesAssert congruent(double modulus, Consumer<DoublesAssert> congruentAssertion) {
        if (modulus == 0.0d) {
            throw this.helper.assertionException("test.error.invalid_modulus");
        }

        congruentAssertion.accept(Assert.doubles(
            this.helper,
            Mth.positiveModulo(this.value, modulus),
            this.name + " (congruent to " + modulus + ")"
        ));
        return this;
    }

    public DoublesAssert equals(double expected) {
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
