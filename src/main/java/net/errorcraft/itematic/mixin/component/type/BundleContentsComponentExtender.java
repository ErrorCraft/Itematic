package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentAccess;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentBuilderAccess;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public class BundleContentsComponentExtender implements BundleContentsComponentAccess {
    @Unique
    private double occupancy;

    @Inject(
        method = "<init>(Ljava/util/List;)V",
        at = @At("TAIL")
    )
    private void calculateOccupancy(List<ItemStack> stacks, CallbackInfo info) {
        for (ItemStack stack : stacks) {
            this.occupancy += stack.itematic$occupancy() * stack.getCount();
        }
    }

    @Override
    public double itematic$occupancy() {
        return this.occupancy;
    }

    @Override
    public void itematic$setOccupancy(double occupancy) {
        this.occupancy = occupancy;
    }

    @Mixin(BundleContentsComponent.Builder.class)
    public static class BuilderExtender implements BundleContentsComponentBuilderAccess {
        @Shadow @Final private List<ItemStack> stacks;
        @Unique
        private int capacity;

        @Unique
        private double occupancy;

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setOccupancy(BundleContentsComponent base, CallbackInfo info) {
            this.occupancy = base.itematic$occupancy();
        }

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private BundleContentsComponent setOccupancy(BundleContentsComponent original) {
            original.itematic$setOccupancy(this.occupancy);
            return original;
        }

        /**
         * @author ErrorCraft
         * @reason Uses doubles to keep precision for max counts for items that are not aligned.
         */
        @Overwrite
        private int getMaxAllowed(ItemStack stack) {
            return (int)((this.capacity - occupancy) / stack.itematic$occupancy());
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
            this.stacks.add(0, stack.split(countToAdd));
        }

        @Inject(
            method = "add(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent;getOccupancy(Lnet/minecraft/item/ItemStack;)I"
            )
        )
        private void addOccupancy(ItemStack stack, CallbackInfoReturnable<Integer> info, @Local int allowedCount) {
            this.occupancy += stack.itematic$occupancy() * allowedCount;
        }

        @Override
        public void itematic$setCapacity(int capacity) {
            this.capacity = capacity;
        }
    }
}
