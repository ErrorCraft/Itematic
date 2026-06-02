package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class EntityTypeAssert<E extends Entity> {
    private final TestContext helper;
    private final EntityType<? extends E> entityType;

    EntityTypeAssert(TestContext helper, EntityType<? extends E> entityType) {
        this.helper = helper;
        this.entityType = entityType;
    }

    public EntityTypeAssert<E> existsAt(BlockPos pos) {
        TestUtil.getSingleEntityAt(this.helper, this.entityType, pos);
        return this;
    }

    public EntityTypeAssert<E> existsAt(BlockPos pos, Consumer<EntityAssert<E>> entityAssertion) {
        return this.existsAt(
            pos,
            Assert::entity,
            entityAssertion
        );
    }

    public <A extends BaseEntityAssert<A, E>> EntityTypeAssert<E> existsAt(BlockPos pos, AssertionSupplier<A, E> assertionSupplier, Consumer<A> entityAssertion) {
        E entity = TestUtil.getSingleEntityAt(this.helper, this.entityType, pos);
        entityAssertion.accept(assertionSupplier.get(this.helper, entity));
        return this;
    }

    public EntityTypeAssert<E> doesNotExist() {
        this.helper.dontExpectEntity(this.entityType);
        return this;
    }

    @FunctionalInterface
    public interface AssertionSupplier<A extends BaseEntityAssert<A, ? extends E>, E extends Entity> {
        A get(TestContext helper, E entity);
    }
}
