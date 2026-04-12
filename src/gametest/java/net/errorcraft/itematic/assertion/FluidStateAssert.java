package net.errorcraft.itematic.assertion;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.test.TestContext;

import java.util.Objects;

public class FluidStateAssert {
    private final TestContext helper;
    private final FluidState state;

    FluidStateAssert(TestContext helper, FluidState state) {
        this.helper = Objects.requireNonNull(helper);
        this.state = Assert.isNotNull(this.helper, state, "fluid state");
    }

    public FluidStateAssert is(Fluid fluid) {
        if (this.state.isOf(fluid)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_type",
            "fluid",
            Registries.FLUID.getId(fluid),
            Registries.FLUID.getId(this.state.getFluid())
        );
    }

    public FluidStateAssert is(TagKey<Fluid> tag) {
        if (this.state.isIn(tag)) {
            return this;
        }

        throw this.helper.createError(
            "test.error.expected_tag",
            "fluid",
            tag.id()
        );
    }
}
