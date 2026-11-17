package net.errorcraft.itematic.assertion;

import net.errorcraft.itematic.util.TestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class EntityTypeAssert<E extends Entity> {
    private final GameTestHelper helper;
    private final EntityType<? extends E> entityType;

    EntityTypeAssert(GameTestHelper helper, EntityType<? extends E> entityType) {
        this.helper = helper;
        this.entityType = entityType;
    }

    public <A extends BaseEntityAssert<A, E>> EntityTypeAssert<E> exists(AssertionSupplier<A, E> assertionSupplier, Consumer<A> entityAssertion) {
        E entity = TestUtil.getSingleEntity(this.helper, this.entityType);
        entityAssertion.accept(assertionSupplier.get(this.helper, entity));
        return this;
    }

    public EntityTypeAssert<E> existsAt(BlockPos pos) {
        TestUtil.getSingleEntityAt(this.helper, this.entityType, pos);
        return this;
    }

    public EntityTypeAssert<E> existsAt(double x, double y, double z) {
        List<? extends E> entities = TestUtil.getEntitiesAt(this.helper, this.entityType, new Vec3(x, y, z));
        if (!entities.isEmpty()) {
            return this;
        }

        throw helper.assertionException(
            "test.error.expected_entity_around",
            this.entityType.getDescription(),
            x,
            y,
            z
        );
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
        this.helper.assertEntityNotPresent(this.entityType);
        return this;
    }

    @FunctionalInterface
    public interface AssertionSupplier<A extends BaseEntityAssert<A, ? extends E>, E extends Entity> {
        A get(GameTestHelper helper, E entity);
    }
}
