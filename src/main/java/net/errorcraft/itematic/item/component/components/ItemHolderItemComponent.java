package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.RejectItemHolderRule;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderItemComponent(Fraction capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) implements ItemComponent<ItemHolderItemComponent> {
    public static final Codec<Fraction> CAPACITY_CODEC = ItematicCodecs.positiveFraction(100);
    public static final Codec<ItemHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CAPACITY_CODEC.fieldOf("capacity").forGetter(ItemHolderItemComponent::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemComponent::rules),
        SoundEvent.CODEC.fieldOf("insert_item_sound").forGetter(ItemHolderItemComponent::insertItemSound),
        SoundEvent.CODEC.fieldOf("remove_item_sound").forGetter(ItemHolderItemComponent::removeItemSound),
        SoundEvent.CODEC.fieldOf("empty_sound").forGetter(ItemHolderItemComponent::emptySound)
    ).apply(instance, ItemHolderItemComponent::new));
    private static final int TICKS_AFTER_FIRST_THROW = BundleItemAccessor.ticksAfterFirstThrow();
    private static final int TICKS_BETWEEN_THROWS = BundleItemAccessor.ticksBetweenThrows();

    public static ItemHolderItemComponent of(int capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) {
        return new ItemHolderItemComponent(Fraction.getFraction(capacity, 1), rules, insertItemSound, removeItemSound, emptySound);
    }

    public static ItemComponent<?>[] of(HolderGetter<Item> items, HolderGetter<SoundEvent> soundEvents) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            UseableItemComponent.builder()
                .useFor(BundleItemAccessor.useDuration())
                .build(),
            of(
                1,
                ItemHolderRules.builder()
                    .rule(RejectItemHolderRule.INSTANCE, ItemPredicate.Builder.item()
                        .itematic$items(items.getOrThrow(ItematicItemTags.BANNED_BUNDLE_ITEMS))
                        .build())
                    .rule(OccupancyHeldItemsWithPenaltyItemHolderRule.of(BundleContentsComponentAccessor.nestedBundleOccupancy()), ItemPredicate.Builder.item()
                        .itematic$behavior(ItemComponentTypes.ITEM_HOLDER)
                        .build())
                    .rule(FractionItemHolderRule.of(Fraction.ONE), ItemPredicate.Builder.item()
                        .withComponents(DataComponentMatchers.Builder.components()
                            .any(DataComponents.BEES)
                            .build())
                        .build())
                    .build(),
                soundEvents.getOrThrow(SoundEventKeys.BUNDLE_INSERT),
                soundEvents.getOrThrow(SoundEventKeys.BUNDLE_REMOVE_ONE),
                soundEvents.getOrThrow(SoundEventKeys.BUNDLE_DROP_CONTENTS)
            )
        };
    }

    @Override
    public ItemComponentType<ItemHolderItemComponent> type() {
        return ItemComponentTypes.ITEM_HOLDER;
    }

    @Override
    public Codec<ItemHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        if (world.isClientSide() || !(user instanceof Player player)) {
            return;
        }

        if (usedTicks == 0 || (usedTicks >= TICKS_AFTER_FIRST_THROW && usedTicks % TICKS_BETWEEN_THROWS == 0)) {
            this.removeAndDrop(stack, player);
        }
    }

    @Override
    public boolean clickOnSlot(ItemStack stack, Slot slot, ClickAction clickType, Player user) {
        if (clickType != ClickAction.SECONDARY) {
            return false;
        }

        BundleContents.Mutable newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return false;
        }

        if (slot.getItem().isEmpty()) {
            this.remove(user, newBuilder, removedStack -> this.add(newBuilder, slot.safeInsert(removedStack), user));
        } else {
            this.add(newBuilder, slot, user);
        }

        stack.set(DataComponents.BUNDLE_CONTENTS, newBuilder.toImmutable());
        return true;
    }

    @Override
    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickAction clickType, Player user, ItemStackExchanger stackExchanger) {
        if (clickType != ClickAction.SECONDARY || !slot.allowModification(user)) {
            return false;
        }

        BundleContents.Mutable newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return false;
        }

        if (cursorStack.isEmpty()) {
            this.remove(user, newBuilder, stackExchanger::exchange);
        } else {
            this.add(newBuilder, cursorStack, user);
        }

        stack.set(DataComponents.BUNDLE_CONTENTS, newBuilder.toImmutable());
        return true;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        builder.set(ItematicDataComponents.ITEM_HOLDER_CAPACITY, this.capacity);
        builder.set(ItematicDataComponents.ITEM_HOLDER_RULES, this.rules);
    }

    public Optional<TooltipComponent> tooltipData(ItemStack stack) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return Optional.empty();
        }

        Fraction capacity = stack.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return Optional.empty();
        }

        BundleTooltip data = new BundleTooltip(bundleContents);
        data.itematic$setCapacity(capacity);
        return Optional.of(data);
    }

    public Fraction occupancy(ItemStack stack) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return null;
        }

        Fraction capacity = stack.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        return bundleContents.itematic$occupancy(rules).divideBy(capacity);
    }

    public void onDestroyed(ItemEntity item) {
        BundleContents bundleContents = item.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            ItemUtils.onContainerDestroyed(item, bundleContents.itemsCopy());
        }
    }

    public BundleContents.Mutable createBuilder(ItemStack stack) {
        BundleContents existingBundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (existingBundleContents == null) {
            return null;
        }

        return this.createBuilder(stack, existingBundleContents);
    }

    public BundleContents.Mutable createBuilder(ItemStack stack, BundleContents existingBundleContents) {
        Fraction capacity = stack.get(ItematicDataComponents.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = stack.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        BundleContents.Mutable newBuilder = new BundleContents.Mutable(existingBundleContents);
        newBuilder.itematic$setExtraFields(existingBundleContents, capacity, rules);
        return newBuilder;
    }

    private void add(BundleContents.Mutable bundleContentsBuilder, ItemStack stack, Player user) {
        int addedCount = bundleContentsBuilder.tryInsert(stack);
        if (addedCount > 0) {
            this.playInsertItemSound(user);
        }
    }

    private void add(BundleContents.Mutable bundleContentsBuilder, Slot slot, Player user) {
        int addedCount = bundleContentsBuilder.tryTransfer(slot, user);
        if (addedCount > 0) {
            this.playInsertItemSound(user);
        }
    }

    private void remove(Entity user, BundleContents.Mutable bundleContentsBuilder, Consumer<ItemStack> onRemoved) {
        ItemStack removedStack = bundleContentsBuilder.removeOne();
        if (removedStack == null) {
            return;
        }

        user.playSound(this.removeItemSound.value(), 0.8f, 0.8f + user.level().getRandom().nextFloat() * 0.4f);
        onRemoved.accept(removedStack);
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
        player.playSound(this.emptySound.value(), 0.8f, 0.8f + player.level().getRandom().nextFloat() * 0.4f);
        player.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));

        stack.set(DataComponents.BUNDLE_CONTENTS, newBuilder.toImmutable());
    }

    private void playInsertItemSound(Player user) {
        user.playSound(this.insertItemSound.value(), 0.8f, 0.8f + user.level().getRandom().nextFloat() * 0.4f);
    }
}
