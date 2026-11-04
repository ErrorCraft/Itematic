package net.errorcraft.itematic.mixin.world.item.component;

import com.google.common.base.Supplier;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.access.world.item.component.BundleContentsAccess;
import net.errorcraft.itematic.access.world.item.component.ContainerComponentAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.SimpleMutableContainer;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.concurrent.Memoizer;
import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(BundleContents.class)
public abstract class BundleContentsExtender implements BundleContentsAccess, ContainerComponentAccess<BundleContents> {
    @Shadow
    @Final
    private List<ItemStackTemplate> items;

    @Shadow
    public abstract Stream<ItemStack> itemCopies();

    @Unique
    private final Memoizer<ItemHolderRules, DataResult<Fraction>> occupancy = ItematicUtil.memoize(this::calculateOccupancy);

    @WrapOperation(
        method = "<init>(Ljava/util/List;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/base/Suppliers;memoize(Lcom/google/common/base/Supplier;)Lcom/google/common/base/Supplier;"
        )
    )
    private Supplier<DataResult<Fraction>> doNotUseOriginalWeightForCachingWithItemHolderRules(Supplier<DataResult<Fraction>> delegate, Operation<Supplier<DataResult<Fraction>>> original) {
        return () -> DataResult.error(() -> "Cannot calculate item holder occupancy without item holder rules");
    }

    @Override
    public Optional<TooltipComponent> itematic$tooltipImage(ItemStack stack) {
        TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        if (!display.shows(DataComponents.BUNDLE_CONTENTS)) {
            return Optional.empty();
        }

        Fraction capacity = stack.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return Optional.empty();
        }

        ItemHolderRules rules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return Optional.empty();
        }

        BundleTooltip data = new BundleTooltip((BundleContents)(Object) this);
        data.itematic$setCapacity(capacity);
        data.itematic$setItemHolderRules(rules);
        return Optional.of(data);
    }

    @Override
    public void itematic$onDestroyed(ItemEntity item) {
        item.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        ItemUtils.onContainerDestroyed(item, this.itemCopies());
    }

    @Override
    public DataResult<Fraction> itematic$occupancy(ItemHolderRules rules) {
        try {
            return this.occupancy.compute(rules);
        } catch (InterruptedException e) {
            return DataResult.error(() -> "Cannot calculate item holder occupancy: " + e.getMessage());
        }
    }

    @Override
    public BundleContents.Mutable itematic$asMutable(Fraction capacity, ItemHolderRules rules) {
        BundleContents.Mutable newContents = new BundleContents.Mutable();
        newContents.itematic$setFields((BundleContents)(Object) this, capacity, rules);
        return newContents;
    }

    @Override
    public BundleContents itematic$copyWithContents(ItemStack stack, Stream<ItemStack> newContents) {
        BundleContents self = (BundleContents)(Object) this;
        ItemHolderRules rules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return self;
        }

        BundleContents.Mutable mutable = rules.createBuilder(stack, self);
        if (mutable == null) {
            return self;
        }

        mutable.clearItems();
        newContents.forEach(mutable::tryInsert);
        return mutable.toImmutable();
    }

    @Unique
    private DataResult<Fraction> calculateOccupancy(ItemHolderRules rules) {
        try {
            Fraction occupancy = Fraction.ZERO;
            for (ItemStackTemplate item : this.items) {
                DataResult<Fraction> itemOccupancy = rules.occupancy(item);
                if (itemOccupancy.isError()) {
                    return itemOccupancy;
                }

                occupancy = occupancy.add(
                    itemOccupancy.getOrThrow()
                        .multiplyBy(Fraction.getFraction(item.count(), 1))
                );
            }

            return DataResult.success(occupancy);
        } catch (ArithmeticException e) {
            return DataResult.error(() -> "Excessive total item holder occupancy");
        }
    }

    @Mixin(BundleContents.Mutable.class)
    public static abstract class MutableExtender extends SimpleMutableContainer<BundleContents> implements MutableAccess {
        @Shadow
        private Fraction weight;

        @Shadow
        private int selectedItem;

        @Unique
        private Fraction capacity;

        @Unique
        private ItemHolderRules rules;

        public MutableExtender(List<ItemStack> items) {
            super(items);
        }

        @WrapOperation(
            method = "getMaxAmountToAdd",
            at = @At(
                value = "FIELD",
                target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private Fraction getCapacity(Operation<Fraction> original) {
            return this.capacity;
        }

        @WrapOperation(
            method = {
                "tryInsert",
                "tryTransfer",
                "removeOne"
            },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/component/BundleContents;getWeight(Lnet/minecraft/world/item/ItemInstance;)Lcom/mojang/serialization/DataResult;"
            )
        )
        private DataResult<Fraction> calculateFromDataComponent(ItemInstance item, Operation<DataResult<Fraction>> original) {
            return this.rules.occupancy(item);
        }

        @WrapOperation(
            method = {
                "tryInsert",
                "tryTransfer"
            },
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/component/BundleContents;canItemBeInBundle(Lnet/minecraft/world/item/ItemStack;)Z"
            )
        )
        private boolean checkFromDataComponent(ItemStack itemToAdd, Operation<Boolean> original) {
            return this.rules.canOccupy(itemToAdd);
        }

        @Inject(
            method = "tryInsert",
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
            this.items.clear();
            DataResult<Fraction> currentWeight = bundleContents.itematic$occupancy(rules);
            if (currentWeight.isError()) {
                this.weight = Fraction.ZERO;
                this.selectedItem = BundleContents.NO_SELECTED_ITEM_INDEX;
            } else {
                for (ItemStackTemplate item : bundleContents.items()) {
                    this.items.add(item.create());
                }

                this.weight = currentWeight.getOrThrow();
                this.selectedItem = bundleContents.getSelectedItemIndex();
            }
        }
    }
}
