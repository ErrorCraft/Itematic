package net.errorcraft.itematic.mixin.world;

import net.errorcraft.itematic.access.world.InteractionResultAccess;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

public interface InteractionResultExtender {
    @Mixin(InteractionResult.Success.class)
    abstract
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
    }
}
