package net.errorcraft.itematic.assertion;

import net.minecraft.entity.ItemEntity;
import net.minecraft.test.TestContext;

import java.util.function.Consumer;

public class ItemEntityAssert extends BaseEntityAssert<ItemEntityAssert, ItemEntity> {
    ItemEntityAssert(TestContext helper, ItemEntity entity) {
        super(helper, entity);
    }

    public ItemEntityAssert itemStack(Consumer<ItemStackAssert> stackAssertion) {
        stackAssertion.accept(Assert.itemStack(this.helper, this.entity.getStack()));
        return this;
    }
}
