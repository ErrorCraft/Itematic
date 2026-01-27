package net.errorcraft.itematic.item;

import net.minecraft.util.ActionResult;

public interface ItemResult {
    // todo: make an enum?
    ItemResult PASS = new Pass();
    ItemResult CONSUME = new Consume();
    ItemResult SUCCESS = new Success();

    ItemResult max(ItemResult other);
    ActionResult toActionResult();
    boolean isAccepted(); // todo remove

    class Pass implements ItemResult {
        private Pass() {}

        @Override
        public ItemResult max(ItemResult other) {
            return other;
        }

        @Override
        public ActionResult toActionResult() {
            return ActionResult.PASS;
        }

        @Override
        public boolean isAccepted() {
            return false;
        }
    }

    class Consume implements ItemResult {
        private Consume() {}

        @Override
        public ItemResult max(ItemResult other) {
            if (other == SUCCESS) {
                return SUCCESS;
            }

            return this;
        }

        @Override
        public ActionResult toActionResult() {
            return ActionResult.CONSUME;
        }

        @Override
        public boolean isAccepted() {
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
        public ActionResult toActionResult() {
            return ActionResult.SUCCESS;
        }

        @Override
        public boolean isAccepted() {
            return true;
        }
    }
}
