package net.errorcraft.itematic.assertion;

import net.minecraft.entity.Entity;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class BaseEntityAssert<A extends BaseEntityAssert<A, E>, E extends Entity> {
    protected final TestContext helper;
    protected final E entity;

    protected BaseEntityAssert(TestContext helper, E entity) {
        this.helper = helper;
        this.entity = entity;
    }

    public A y(Consumer<DoublesAssert> yAssertion) {
        yAssertion.accept(Assert.doubles(this.helper, this.entity.getY(), "y"));
        return (A) this;
    }

    public A yaw(Consumer<FloatsAssert> yawAssertion) {
        yawAssertion.accept(Assert.floats(this.helper, MathHelper.wrapDegrees(this.entity.getYaw()), "yaw"));
        return (A) this;
    }

    public <P> A test(Function<E, P> propertySupplier, Consumer<P> propertyConsumer) {
        propertyConsumer.accept(propertySupplier.apply(this.entity));
        return (A) this;
    }
}
