package net.errorcraft.itematic.mixin.world.item.component;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.item.component.BundleContentsAccess;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.class)
public class BundleContentsExtender implements BundleContentsAccess {
    @Shadow
    @Final
    private List<ItemStack> items;

    @Shadow
    @Final
    @Mutable
    @Nullable
    private Fraction weight;

    @Redirect(
        method = "<init>(Ljava/util/List;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/BundleContents;computeContentWeight(Ljava/util/List;)Lorg/apache/commons/lang3/math/Fraction;"
        )
    )
    @Nullable
    private static Fraction doNotCalculateOccupancyForLaterCaching(List<ItemStack> items) {
        return null;
    }

    @WrapOperation(
        method = "equals",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/commons/lang3/math/Fraction;equals(Ljava/lang/Object;)Z"
        )
    )
    private boolean doNotTestWeight(Fraction instance, Object obj, Operation<Boolean> original) {
        return true;
    }

    @Override
    public Fraction itematic$occupancy(ItemHolderRules rules) {
        if (this.weight != null) {
            return this.weight;
        }

        Fraction occupancy = Fraction.ZERO;
        for (ItemStack stack : this.items) {
            occupancy = occupancy.add(rules.occupancy(stack)
                .multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
        }

        return this.weight = occupancy;
    }

    @Mixin(BundleContents.Mutable.class)
    public static class MutableExtender implements MutableAccess {
        @Shadow
        @Final
        private List<ItemStack> items;

        @Shadow
        private Fraction weight;

        @Unique
        private Fraction capacity;

        @Unique
        private ItemHolderRules rules;

        @Redirect(
            method = "getMaxAmountToAdd",
            at = @At(
                value = "FIELD",
                target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private Fraction getCapacity() {
            return this.capacity;
        }

        @WrapMethod(
            method = "getMaxAmountToAdd"
        )
        private int checkCanOccupy(ItemStack item, Operation<Integer> original) {
            if (!this.rules.canOccupy(item)) {
                return 0;
            }

            return original.call(item);
        }

        @Redirect(
            method = {
                "getMaxAmountToAdd",
                "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
                "removeOne"
            },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/component/BundleContents;getWeight(Lnet/minecraft/world/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"
            )
        )
        private Fraction calculateFromDataComponent(ItemStack stack) {
            return this.rules.occupancy(stack);
        }

        @Redirect(
            method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/component/BundleContents;canItemBeInBundle(Lnet/minecraft/world/item/ItemStack;)Z"
            )
        )
        private boolean checkFromDataComponent(ItemStack itemsToAdd) {
            return this.rules.canOccupy(itemsToAdd);
        }

        @Inject(
            method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;findStackIndex(Lnet/minecraft/world/item/ItemStack;)I"
            ),
            cancellable = true
        )
        private void splitOverMultipleItemStacks(ItemStack itemsToAdd, CallbackInfoReturnable<Integer> info, @Local(name = "amountToAdd") int amountToAdd) {
            // The assumption that an overflowing item stack doesn't fit no longer applies due to data-driven occupancies
            info.setReturnValue(amountToAdd);
            for (ItemStack heldStack : this.items) {
                if (!ItemStack.isSameItemSameComponents(heldStack, itemsToAdd)) {
                    continue;
                }

                int count = Math.min(heldStack.getMaxStackSize() - heldStack.getCount(), amountToAdd);
                heldStack.grow(count);
                itemsToAdd.shrink(count);
                amountToAdd -= count;
                if (amountToAdd <= 0) {
                    return;
                }
            }

            this.items.addFirst(itemsToAdd.split(amountToAdd));
        }

        @Override
        public void itematic$setFields(BundleContents bundleContents, Fraction capacity, ItemHolderRules rules) {
            this.capacity = capacity;
            this.rules = rules;
            this.weight = bundleContents.itematic$occupancy(rules);
        }
    }
}
