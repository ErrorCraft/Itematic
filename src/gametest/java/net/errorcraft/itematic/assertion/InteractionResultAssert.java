package net.errorcraft.itematic.assertion;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class InteractionResultAssert {
    private final GameTestHelper helper;
    private final InteractionResult interactionResult;
    private final String name;

    InteractionResultAssert(GameTestHelper helper, InteractionResult interactionResult, String name) {
        this.helper = helper;
        this.interactionResult = interactionResult;
        this.name = name;
    }

    public InteractionResultAssert resultStack(Consumer<ItemStackAssert> stackAssertion) {
        if (this.interactionResult instanceof InteractionResult.Success success) {
            ItemStack newStack = success.heldItemTransformedTo();
            stackAssertion.accept(Assert.itemStack(this.helper, newStack, "new stack for " + this.name));
            return this;
        }

        throw this.helper.assertionException(
            "test.error.expected_success",
            this.name
        );
    }
}
