package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.BundleItemAccessor;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.holder.ItemHolderSounds;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderItemBehavior(Fraction capacity, ItemHolderRules rules, ItemHolderSounds sounds) implements ItemBehavior<ItemHolderItemBehavior> {
    public static final Codec<Fraction> CAPACITY_CODEC = ItematicCodecs.positiveFraction(100);
    public static final Codec<ItemHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CAPACITY_CODEC.fieldOf("capacity").forGetter(ItemHolderItemBehavior::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemBehavior::rules),
        ItemHolderSounds.CODEC.fieldOf("sounds").forGetter(ItemHolderItemBehavior::sounds)
    ).apply(instance, ItemHolderItemBehavior::new));
    private static final int TICKS_AFTER_FIRST_THROW = BundleItemAccessor.ticksAfterFirstThrow();
    private static final int TICKS_BETWEEN_THROWS = BundleItemAccessor.ticksBetweenThrows();

    public static ItemHolderItemBehavior of(Fraction capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> insertFailItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) {
        return new ItemHolderItemBehavior(
            capacity,
            rules,
            new ItemHolderSounds(insertItemSound, insertFailItemSound, removeItemSound, emptySound)
        );
    }

    @Override
    public ItemBehaviorType<ItemHolderItemBehavior> type() {
        return ItemBehaviorType.ITEM_HOLDER;
    }

    @Override
    public void using(ItemStack stack, Level level, LivingEntity user, int usedTicks, int remainingUseTicks) {
        if (level.isClientSide() || !(user instanceof Player player)) {
            return;
        }

        if (usedTicks == 0 || (usedTicks >= TICKS_AFTER_FIRST_THROW && usedTicks % TICKS_BETWEEN_THROWS == 0)) {
            this.removeAndDrop(stack, player);
        }
    }

    @Override
    public boolean clickOnSlot(ItemStack stack, Slot slot, ClickAction clickAction, Player user) {
        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return false;
        }

        ItemStack other = slot.getItem();
        if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
            this.transfer(stack, newContents, slot, user);
            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
            this.removeAndAddRemainderBack(stack, newContents, slot, user);
            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickAction clickAction, Player user, ItemStackExchanger stackExchanger) {
        if (clickAction == ClickAction.PRIMARY && cursorStack.isEmpty()) {
            this.toggleItem(stack, BundleContents.NO_SELECTED_ITEM_INDEX);
            return false;
        }

        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return false;
        }

        if (clickAction == ClickAction.PRIMARY && !cursorStack.isEmpty()) {
            if (slot.allowModification(user)) {
                this.add(stack, newContents, slot.safeInsert(cursorStack), user);
            }

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (clickAction == ClickAction.SECONDARY && cursorStack.isEmpty()) {
            if (slot.allowModification(user)) {
                this.remove(stack, user, newContents, stackExchanger::exchange);
            }

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        this.toggleItem(stack, BundleContents.NO_SELECTED_ITEM_INDEX);
        return false;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        builder.set(ItematicDataComponents.ITEM_HOLDER_CAPACITY, this.capacity);
        builder.set(ItematicDataComponents.ITEM_HOLDER_RULES, this.rules);
        builder.set(ItematicDataComponents.ITEM_HOLDER_SOUNDS, this.sounds);
    }

    public Optional<TooltipComponent> tooltipData(ItemStack stack) {
        TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        if (!display.shows(DataComponents.BUNDLE_CONTENTS)) {
            return Optional.empty();
        }

        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents == null) {
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

        BundleTooltip data = new BundleTooltip(bundleContents);
        data.itematic$setCapacity(capacity);
        data.itematic$setItemHolderRules(rules);
        return Optional.of(data);
    }

    @Nullable
    public static DataResult<Fraction> occupancy(ItemInstance item) {
        BundleContents bundleContents = item.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return null;
        }

        Fraction capacity = item.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = item.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        return bundleContents.itematic$occupancy(rules)
            .map(fraction -> fraction.divideBy(capacity));
    }

    public void onDestroyed(ItemEntity item) {
        BundleContents bundleContents = item.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            ItemUtils.onContainerDestroyed(item, bundleContents.itemCopies());
        }
    }

    public BundleContents.@Nullable Mutable createBuilder(ItemStack stack) {
        BundleContents existingBundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (existingBundleContents == null) {
            return null;
        }

        return this.createBuilder(stack, existingBundleContents);
    }

    public BundleContents.@Nullable Mutable createBuilder(ItemStack stack, BundleContents existingBundleContents) {
        Fraction capacity = stack.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        return existingBundleContents.itematic$asMutable(capacity, rules);
    }

    public static void toggleSelectedItem(ItemStack stack, int selectedItem) {
        stack.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .ifPresent(itemHolder -> itemHolder.toggleItem(stack, selectedItem));
    }

    private void toggleItem(ItemStack stack, int selectedItem) {
        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return;
        }

        newContents.toggleSelectedItem(selectedItem);
        stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
    }

    private void add(ItemStack stack, BundleContents.Mutable newContents, ItemStack stackToInsert, Player user) {
        int addedCount = newContents.tryInsert(stackToInsert);
        if (addedCount > 0) {
            ItemHolderSounds.playInsertItemSound(stack, user);
        } else {
            ItemHolderSounds.playInsertItemFailSound(stack, user);
        }
    }

    private void removeAndDrop(ItemStack stack, Player player) {
        BundleContents.Mutable newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return;
        }

        ItemStack removedStack = newBuilder.removeOne();
        if (removedStack == null) {
            return;
        }

        player.drop(removedStack, true, Prediction.PREDICTED);
        ItemHolderSounds.playEmptySound(stack, player);
        player.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        stack.set(DataComponents.BUNDLE_CONTENTS, newBuilder.toImmutable());
    }

    private void transfer(ItemStack stack, BundleContents.Mutable newContents, Slot slot, Player user) {
        int transferredCount = newContents.tryTransfer(slot, user);
        if (transferredCount > 0) {
            ItemHolderSounds.playInsertItemSound(stack, user);
        } else {
            ItemHolderSounds.playInsertItemFailSound(stack, user);
        }
    }

    private void remove(ItemStack stack, Entity user, BundleContents.Mutable newContents, Consumer<ItemStack> onRemoved) {
        ItemStack removedStack = newContents.removeOne();
        if (removedStack == null) {
            return;
        }

        ItemHolderSounds.playRemoveItemSound(stack, user);
        onRemoved.accept(removedStack);
    }

    private void removeAndAddRemainderBack(ItemStack stack, BundleContents.Mutable newContents, Slot slot, Player user) {
        ItemStack removedStack = newContents.removeOne();
        if (removedStack == null) {
            return;
        }

        ItemStack remainder = slot.safeInsert(removedStack);
        if (remainder.isEmpty()) {
            ItemHolderSounds.playRemoveItemSound(stack, user);
        } else {
            newContents.tryInsert(remainder);
        }
    }

    private static void broadcastSlotsChanged(Player user) {
        user.containerMenu.slotsChanged(user.getInventory());
    }
}
