package net.errorcraft.itematic.assertion;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Objects;

public class FluidStateAssert {
    private final GameTestHelper helper;
    private final FluidState state;

    FluidStateAssert(GameTestHelper helper, FluidState state) {
        this.helper = Objects.requireNonNull(helper);
        this.state = Assert.isNotNull(this.helper, state, "fluid state");
    }

    public FluidStateAssert is(Fluid fluid) {
        if (this.state.is(fluid)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_type",
            "fluid",
            BuiltInRegistries.FLUID.getKey(fluid),
            BuiltInRegistries.FLUID.getKey(this.state.getType())
        );
    }

    public FluidStateAssert is(TagKey<Fluid> tag) {
        if (this.state.is(tag)) {
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_tag",
            "fluid",
            tag.location()
        );
    }
}
