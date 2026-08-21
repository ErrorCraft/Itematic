package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.function.Consumer;

public class ItemEntityAssert extends BaseEntityAssert<ItemEntityAssert, ItemEntity> {
    ItemEntityAssert(GameTestHelper helper, ItemEntity entity) {
        super(helper, entity);
    }

    public ItemEntityAssert itemStack(Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getItem()));
        return this;
    }
}
