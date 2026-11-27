package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.InteractionResultAccess;
import net.errorcraft.itematic.world.InteractionResults;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

public interface InteractionResultExtender {
    @Mixin(InteractionResult.Fail.class)
    class FailExtender implements InteractionResultAccess {
        @Override
        public InteractionResult itematic$merge(InteractionResult that) {
            // Only fail if the other result passes:
            // - FAIL + PASS  -> FAIL
            // - FAIL + other -> other
            return that == InteractionResult.PASS
                ? (InteractionResult)(Object) this
                : that;
        }
    }

    @Mixin(InteractionResult.Pass.class)
    class PassExtender implements InteractionResultAccess {
        @Override
        public InteractionResult itematic$merge(InteractionResult that) {
            // Every other result has priority over passing
            // - This includes failing; See FailExtender for commutativity
            return that;
        }
    }

    @Mixin(InteractionResult.TryEmptyHandInteraction.class)
    class TryEmptyHandInteractionExtender implements InteractionResultAccess {
        @Override
        public InteractionResult itematic$merge(InteractionResult that) {
            return that == InteractionResult.PASS || that == InteractionResult.FAIL
                ? (InteractionResult)(Object) this
                : that;
        }
    }

    @Mixin(InteractionResult.Success.class)
    class SuccessExtender implements InteractionResultAccess {
        @Shadow
        @Final
        private InteractionResult.SwingSource swingSource;

        @Shadow
        @Final
        private InteractionResult.ItemContext itemContext;

        @Override
        public InteractionResult itematic$exchanged(Consumer<ItemStack> stackExchanger) {
            ItemStack transformedStack = this.itemContext.heldItemTransformedTo();
            if (transformedStack != null) {
                stackExchanger.accept(transformedStack);
            }

            return new InteractionResult.Success(this.swingSource, InteractionResult.ItemContext.DEFAULT);
        }

        @Override
        public InteractionResult itematic$merge(InteractionResult that) {
            // Success has priority over every other result
            // - However, we do still need to consider the swing source part of the success
            if (!((Object) that instanceof InteractionResult.Success thatSuccess)) {
                return (InteractionResult)(Object) this;
            }

            return InteractionResults.pickSuccess(
                (InteractionResult.Success)(Object) this,
                thatSuccess
            );
        }
    }
}
