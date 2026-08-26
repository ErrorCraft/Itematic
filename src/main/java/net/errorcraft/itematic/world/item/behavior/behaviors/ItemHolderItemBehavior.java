package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.BundleItemAccessor;
import net.errorcraft.itematic.mixin.world.item.component.BundleContentsAccessor;
import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.world.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.RejectItemHolderRule;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
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

public record ItemHolderItemBehavior(Fraction capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> insertFailItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) implements ItemBehavior<ItemHolderItemBehavior> {
    public static final Codec<Fraction> CAPACITY_CODEC = ItematicCodecs.positiveFraction(100);
    public static final Codec<ItemHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CAPACITY_CODEC.fieldOf("capacity").forGetter(ItemHolderItemBehavior::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemBehavior::rules),
        SoundEvent.CODEC.fieldOf("insert_item_sound").forGetter(ItemHolderItemBehavior::insertItemSound),
        SoundEvent.CODEC.fieldOf("insert_fail_item_sound").forGetter(ItemHolderItemBehavior::insertFailItemSound),
        SoundEvent.CODEC.fieldOf("remove_item_sound").forGetter(ItemHolderItemBehavior::removeItemSound),
        SoundEvent.CODEC.fieldOf("empty_sound").forGetter(ItemHolderItemBehavior::emptySound)
    ).apply(instance, ItemHolderItemBehavior::new));
    private static final int TICKS_AFTER_FIRST_THROW = BundleItemAccessor.ticksAfterFirstThrow();
    private static final int TICKS_BETWEEN_THROWS = BundleItemAccessor.ticksBetweenThrows();

    public static ItemHolderItemBehavior of(int capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> insertFailItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) {
        return new ItemHolderItemBehavior(Fraction.getFraction(capacity, 1), rules, insertItemSound, insertFailItemSound, removeItemSound, emptySound);
    }

    public static ItemBehavior<?>[] of(HolderGetter<Item> items, HolderGetter<SoundEvent> soundEvents) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            UseableItemBehavior.builder()
                .useFor(BundleItemAccessor.useDuration())
                .build(),
            of(
                1,
                ItemHolderRules.builder()
                    .rule(
                        RejectItemHolderRule.INSTANCE,
                        ItemPredicate.Builder.item()
                            .itematic$items(items.getOrThrow(ItematicItemTags.BANNED_BUNDLE_ITEMS))
                            .build()
                    )
                    .rule(
                        OccupancyHeldItemsWithPenaltyItemHolderRule.of(BundleContentsAccessor.nestedBundleOccupancy()),
                        ItemPredicate.Builder.item()
                            .withComponents(DataComponentMatchers.Builder.components()
                                .any(DataComponents.BUNDLE_CONTENTS)
                                .build())
                            .build()
                    )
                    .rule(
                        FractionItemHolderRule.of(Fraction.ONE),
                        ItemPredicate.Builder.item()
                            .withComponents(DataComponentMatchers.Builder.components()
                                .any(DataComponents.BEES)
                                .build())
                            .build()
                    )
                    .build(),
                soundEvents.getOrThrow(SoundEventIds.BUNDLE_INSERT),
                soundEvents.getOrThrow(SoundEventIds.BUNDLE_INSERT_FAIL),
                soundEvents.getOrThrow(SoundEventIds.BUNDLE_REMOVE_ONE),
                soundEvents.getOrThrow(SoundEventIds.BUNDLE_DROP_CONTENTS)
            )
        };
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
            this.transfer(newContents, slot, user);
            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
            this.removeAndAddRemainderBack(newContents, slot, user);
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
                this.add(newContents, slot.safeInsert(cursorStack), user);
            }

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (clickAction == ClickAction.SECONDARY && cursorStack.isEmpty()) {
            if (slot.allowModification(user)) {
                this.remove(user, newContents, stackExchanger::exchange);
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
            ItemUtils.onContainerDestroyed(item, bundleContents.itemCopyStream());
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

        BundleContents.Mutable newContents = new BundleContents.Mutable(existingBundleContents);
        newContents.itematic$setFields(existingBundleContents, capacity, rules);
        return newContents;
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

    private void add(BundleContents.Mutable newContents, ItemStack stack, Player user) {
        int addedCount = newContents.tryInsert(stack);
        if (addedCount > 0) {
            this.playInsertItemSound(user);
        } else {
            this.playInsertFailSound(user);
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

        player.drop(removedStack, true);
        player.playSound(
            this.emptySound.value(),
            0.8f,
            0.8f + player.level().getRandom().nextFloat() * 0.4f
        );
        player.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        stack.set(DataComponents.BUNDLE_CONTENTS, newBuilder.toImmutable());
    }

    private void transfer(BundleContents.Mutable newContents, Slot slot, Player user) {
        int transferredCount = newContents.tryTransfer(slot, user);
        if (transferredCount > 0) {
            this.playInsertItemSound(user);
        } else {
            this.playInsertFailSound(user);
        }
    }

    private void remove(Entity user, BundleContents.Mutable newContents, Consumer<ItemStack> onRemoved) {
        ItemStack removedStack = newContents.removeOne();
        if (removedStack == null) {
            return;
        }

        this.playRemoveOneSound(user);
        onRemoved.accept(removedStack);
    }

    private void removeAndAddRemainderBack(BundleContents.Mutable newContents, Slot slot, Player user) {
        ItemStack removedStack = newContents.removeOne();
        if (removedStack == null) {
            return;
        }

        ItemStack remainder = slot.safeInsert(removedStack);
        if (remainder.isEmpty()) {
            this.playRemoveOneSound(user);
        } else {
            newContents.tryInsert(remainder);
        }
    }

    private void playInsertItemSound(Entity user) {
        user.playSound(
            this.insertItemSound.value(),
            0.8f,
            0.8f + user.level().getRandom().nextFloat() * 0.4f
        );
    }

    private void playInsertFailSound(Entity user) {
        user.playSound(
            this.insertFailItemSound.value(),
            1.0f,
            1.0f
        );
    }

    private void playRemoveOneSound(Entity user) {
        user.playSound(
            this.removeItemSound.value(),
            0.8f,
            0.8f + user.level().getRandom().nextFloat() * 0.4f
        );
    }

    private static void broadcastSlotsChanged(Player user) {
        user.containerMenu.slotsChanged(user.getInventory());
    }
}
