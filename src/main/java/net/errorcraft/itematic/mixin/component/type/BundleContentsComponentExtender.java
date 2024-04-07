package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentBuilderAccess;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public class BundleContentsComponentExtender {
    @Mixin(BundleContentsComponent.Builder.class)
    public static class BuilderExtender implements BundleContentsComponentBuilderAccess {
        @Shadow
        @Final
        private List<ItemStack> stacks;

        @Unique
        private Fraction capacity;

        @Redirect(
            method = "getMaxAllowed",
            at = @At(
                value = "FIELD",
                target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;"
            )
        )
        private Fraction getCapacity() {
            return this.capacity;
        }

        @Inject(
            method = "add(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;addInternal(Lnet/minecraft/item/ItemStack;)I"
            ),
            cancellable = true
        )
        private void splitOverMultipleItemStacks(ItemStack stack, CallbackInfoReturnable<Integer> info, @Local int countToAdd) {
            info.setReturnValue(countToAdd);
            for (ItemStack heldStack : this.stacks) {
                if (!ItemStack.areItemsAndComponentsEqual(heldStack, stack)) {
                    continue;
                }
                int count = Math.min(heldStack.getMaxCount() - heldStack.getCount(), countToAdd);
                heldStack.increment(count);
                stack.decrement(count);
                countToAdd -= count;
                if (countToAdd <= 0) {
                    return;
                }
            }
            this.stacks.addFirst(stack.split(countToAdd));
        }

        @Override
        public void itematic$setCapacity(int capacity) {
            this.capacity = Fraction.getFraction(capacity, Item.DEFAULT_MAX_COUNT);
        }
    }
}
