package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;

public class EntityAssert<E extends Entity> extends BaseEntityAssert<EntityAssert<E>, E> {
    EntityAssert(GameTestHelper helper, E entity) {
        super(helper, entity);
    }
}
