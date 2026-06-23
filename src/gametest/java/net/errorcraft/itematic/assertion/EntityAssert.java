package net.errorcraft.itematic.assertion;

import net.minecraft.entity.Entity;
import net.minecraft.test.TestContext;

public class EntityAssert<E extends Entity> extends BaseEntityAssert<EntityAssert<E>, E> {
    EntityAssert(TestContext helper, E entity) {
        super(helper, entity);
    }
}
