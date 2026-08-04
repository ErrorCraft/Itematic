package net.errorcraft.itematic.item;

import net.minecraft.world.InteractionResult;

public interface ItemResult {
    ItemResult PASS = new Pass();
    ItemResult CONSUME = new Consume();
    ItemResult SUCCEED = new Success();

    ItemResult max(ItemResult other);
    InteractionResult toActionResult();
    boolean succeeds();

    class Pass implements ItemResult {
        private Pass() {}

        @Override
        public ItemResult max(ItemResult other) {
            return other;
        }

        @Override
        public InteractionResult toActionResult() {
            return InteractionResult.PASS;
        }

        @Override
        public boolean succeeds() {
            return false;
        }
    }

    class Consume implements ItemResult {
        private Consume() {}

        @Override
        public ItemResult max(ItemResult other) {
            if (other == SUCCEED) {
                return SUCCEED;
            }

            return this;
        }

        @Override
        public InteractionResult toActionResult() {
            return InteractionResult.CONSUME;
        }

        @Override
        public boolean succeeds() {
            return true;
        }
    }

    class Success implements ItemResult {
        private Success() {}

        @Override
        public ItemResult max(ItemResult other) {
            return this;
        }

        @Override
        public InteractionResult toActionResult() {
            return InteractionResult.SUCCESS;
        }

        @Override
        public boolean succeeds() {
            return true;
        }
    }
}
