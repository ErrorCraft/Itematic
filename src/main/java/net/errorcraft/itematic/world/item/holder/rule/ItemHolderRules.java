package net.errorcraft.itematic.world.item.holder.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.advancements.predicates.ItemPredicates;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.item.BundleItemAccessor;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.holder.ItemHolderSounds;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderRules(List<Rule> rules) {
    public static final Codec<ItemHolderRules> CODEC = Rule.CODEC.listOf().xmap(ItemHolderRules::new, ItemHolderRules::rules);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemHolderRules> STREAM_CODEC = Rule.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemHolderRules::new, ItemHolderRules::rules);
    private static final int TICKS_AFTER_FIRST_THROW = BundleItemAccessor.ticksAfterFirstThrow();
    private static final int TICKS_BETWEEN_THROWS = BundleItemAccessor.ticksBetweenThrows();

    public static Builder builder() {
        return new Builder();
    }

    public void tryDrop(ItemStack stack, Level level, LivingEntity user, int usedTicks) {
        if (level.isClientSide() || !(user instanceof Player player)) {
            return;
        }

        if (usedTicks == 0 || (usedTicks >= TICKS_AFTER_FIRST_THROW && usedTicks % TICKS_BETWEEN_THROWS == 0)) {
            this.removeAndDrop(stack, player);
        }
    }

    public boolean clickOnSlot(ItemStack stack, Slot slot, ClickAction action, Player user) {
        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return false;
        }

        ItemStack slotStack = slot.getItem();
        if (action == ClickAction.PRIMARY && !slotStack.isEmpty()) {
            this.transfer(stack, newContents, slot, user);
            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (action == ClickAction.SECONDARY && slotStack.isEmpty()) {
            this.removeAndAddRemainderBack(stack, newContents, slot, user);
            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        return false;
    }

    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickAction action, Player user, ItemStackExchanger stackExchanger) {
        if (action == ClickAction.PRIMARY && cursorStack.isEmpty()) {
            this.toggleItem(stack, BundleContents.NO_SELECTED_ITEM_INDEX);
            return false;
        }

        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return false;
        }

        if (action == ClickAction.PRIMARY && !cursorStack.isEmpty()) {
            if (slot.allowModification(user)) {
                this.add(stack, newContents, slot.safeInsert(cursorStack), user);
            }

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
            broadcastSlotsChanged(user);
            return true;
        }

        if (action == ClickAction.SECONDARY && cursorStack.isEmpty()) {
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

    public void toggleItem(ItemStack stack, int selectedItem) {
        BundleContents.Mutable newContents = this.createBuilder(stack);
        if (newContents == null) {
            return;
        }

        newContents.toggleSelectedItem(selectedItem);
        stack.set(DataComponents.BUNDLE_CONTENTS, newContents.toImmutable());
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

    public DataResult<Fraction> occupancy(ItemInstance item) {
        for (Rule rule : this.rules) {
            if (rule.test(item)) {
                return rule.rule.occupancy(item);
            }
        }

        return DataResult.success(Fraction.getFraction(1, item.getMaxStackSize()));
    }

    public boolean canOccupy(ItemInstance item) {
        for (Rule rule : this.rules) {
            if (rule.test(item)) {
                return rule.rule.canOccupy(item);
            }
        }

        return true;
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

    public static class Builder {
        private final List<Rule> rules = new ArrayList<>();

        public ItemHolderRules build() {
            return new ItemHolderRules(this.rules);
        }

        public Builder rule(ItemHolderRule rule) {
            this.rules.add(new Rule(Optional.empty(), rule));
            return this;
        }

        public Builder rule(ItemHolderRule rule, ItemPredicate condition) {
            this.rules.add(new Rule(Optional.of(condition), rule));
            return this;
        }
    }

    public record Rule(Optional<ItemPredicate> condition, ItemHolderRule rule) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemPredicate.CODEC.optionalFieldOf("condition").forGetter(Rule::condition),
            ItemHolderRule.CODEC.forGetter(Rule::rule)
        ).apply(instance, Rule::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
            ItemPredicates.STREAM_CODEC.apply(ByteBufCodecs::optional), Rule::condition,
            ItemHolderRule.STREAM_CODEC, Rule::rule,
            Rule::new
        );

        public boolean test(ItemInstance stack) {
            return this.condition.map(predicate -> predicate.test(stack))
                .orElse(true);
        }
    }
}
