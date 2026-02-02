package net.errorcraft.itematic.mixin.component.type;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentAccess;
import net.errorcraft.itematic.access.component.type.BundleContentsComponentBuilderAccess;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public class BundleContentsComponentExtender implements BundleContentsComponentAccess {
    @Shadow
    @Final
    List<ItemStack> stacks;

    @Shadow
    @Final
    @Mutable
    Fraction occupancy;

    @Redirect(
        method = "<init>(Ljava/util/List;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/BundleContentsComponent;calculateOccupancy(Ljava/util/List;)Lorg/apache/commons/lang3/math/Fraction;"
        )
    )
    private static Fraction doNotCalculateOccupancyForLaterCaching(List<ItemStack> stacks) {
        return null;
    }

    @Redirect(
        method = "equals",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/commons/lang3/math/Fraction;equals(Ljava/lang/Object;)Z",
            remap = false
        ),
        remap = false
    )
    private boolean doNotTestOccupancy(Fraction instance, Object obj) {
        return true;
    }

    @Override
    public Fraction itematic$occupancy(ItemHolderRules rules) {
        if (this.occupancy != null) {
            return this.occupancy;
        }

        Fraction occupancy = Fraction.ZERO;
        for (ItemStack stack : this.stacks) {
            occupancy = occupancy.add(rules.occupancy(stack)
                .multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
        }

        return this.occupancy = occupancy;
    }

    @Mixin(BundleContentsComponent.Builder.class)
    public static class BuilderExtender implements BundleContentsComponentBuilderAccess {
        @Shadow
        @Final
        private List<ItemStack> stacks;

        @Shadow
        private Fraction occupancy;

        @Unique
        private Fraction capacity;

        @Unique
        private ItemHolderRules rules;

        @Redirect(
            method = "getMaxAllowed",
            at = @At(
                value = "FIELD",
                target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
                opcode = Opcodes.GETSTATIC,
                remap = false
            )
        )
        private Fraction getCapacity() {
            return this.capacity;
        }

        @Inject(
            method = "getMaxAllowed",
            at = @At("HEAD"),
            cancellable = true
        )
        private void checkCanOccupy(ItemStack stack, CallbackInfoReturnable<Integer> info) {
            if (!this.rules.canOccupy(stack)) {
                info.setReturnValue(0);
            }
        }

        @Redirect(
            method = { "getMaxAllowed", "add(Lnet/minecraft/item/ItemStack;)I", "removeFirst" },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent;getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"
            )
        )
        private Fraction calculateFromDataComponent(ItemStack stack) {
            return this.rules.occupancy(stack);
        }

        @Redirect(
            method = "add(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/BundleContentsComponent;canBeBundled(Lnet/minecraft/item/ItemStack;)Z"
            )
        )
        private boolean checkFromDataComponent(ItemStack stack) {
            return this.rules.canOccupy(stack);
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
        public void itematic$setExtraFields(BundleContentsComponent bundleContents, Fraction capacity, ItemHolderRules rules) {
            this.capacity = capacity;
            this.rules = rules;
            this.occupancy = bundleContents.itematic$occupancy(rules);
        }
    }
}
