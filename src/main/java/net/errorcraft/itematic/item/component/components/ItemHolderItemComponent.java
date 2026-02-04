package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.BundleTooltipData;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderItemComponent(Fraction capacity, ItemHolderRules rules, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) implements ItemComponent<ItemHolderItemComponent> {
    public static final Codec<Fraction> CAPACITY_CODEC = ItematicCodecs.positiveFraction(100);
    public static final Codec<ItemHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CAPACITY_CODEC.fieldOf("capacity").forGetter(ItemHolderItemComponent::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemComponent::rules),
        SoundEvent.ENTRY_CODEC.fieldOf("insert_item_sound").forGetter(ItemHolderItemComponent::insertItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("remove_item_sound").forGetter(ItemHolderItemComponent::removeItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("empty_sound").forGetter(ItemHolderItemComponent::emptySound)
    ).apply(instance, ItemHolderItemComponent::new));
    private static final int TICKS_AFTER_FIRST_THROW = BundleItemAccessor.ticksAfterFirstThrow();
    private static final int TICKS_BETWEEN_THROWS = BundleItemAccessor.ticksBetweenThrows();

    public static ItemHolderItemComponent of(int capacity, ItemHolderRules rules, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) {
        return new ItemHolderItemComponent(Fraction.getFraction(capacity, 1), rules, insertItemSound, removeItemSound, emptySound);
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
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        if (world.isClient() || !(user instanceof PlayerEntity player)) {
            return;
        }

        if (usedTicks == 0 || (usedTicks >= TICKS_AFTER_FIRST_THROW && usedTicks % TICKS_BETWEEN_THROWS == 0)) {
            this.removeAndDrop(stack, player);
        }
    }

    @Override
    public boolean clickOnSlot(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity user) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }

        BundleContentsComponent.Builder newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return false;
        }

        if (slot.getStack().isEmpty()) {
            this.remove(user, newBuilder, removedStack -> this.add(newBuilder, slot.insertStack(removedStack), user));
        } else {
            this.add(newBuilder, slot, user);
        }

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, newBuilder.build());
        return true;
    }

    @Override
    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickType clickType, PlayerEntity user, ItemStackConsumer resultStackConsumer) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(user)) {
            return false;
        }

        BundleContentsComponent.Builder newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return false;
        }

        if (cursorStack.isEmpty()) {
            this.remove(user, newBuilder, resultStackConsumer::set);
        } else {
            this.add(newBuilder, cursorStack, user);
        }

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, newBuilder.build());
        return true;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        builder.add(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY, this.capacity);
        builder.add(ItematicDataComponentTypes.ITEM_HOLDER_RULES, this.rules);
    }

    public Optional<TooltipData> tooltipData(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return Optional.empty();
        }

        Fraction capacity = stack.get(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return Optional.empty();
        }

        BundleTooltipData data = new BundleTooltipData(bundleContents);
        data.itematic$setCapacity(capacity);
        return Optional.of(data);
    }

    public Fraction occupancy(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return null;
        }

        Fraction capacity = stack.get(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = stack.get(ItematicDataComponentTypes.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        return bundleContents.itematic$occupancy(rules).divideBy(capacity);
    }

    public void onDestroyed(ItemEntity item) {
        BundleContentsComponent bundleContents = item.getStack().get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            ItemUsage.spawnItemContents(item, bundleContents.iterateCopy());
        }
    }

    public BundleContentsComponent.Builder createBuilder(ItemStack stack) {
        BundleContentsComponent existingBundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (existingBundleContents == null) {
            return null;
        }

        return this.createBuilder(stack, existingBundleContents);
    }

    public BundleContentsComponent.Builder createBuilder(ItemStack stack, BundleContentsComponent existingBundleContents) {
        Fraction capacity = stack.get(ItematicDataComponentTypes.ITEM_HOLDER_CAPACITY);
        if (capacity == null) {
            return null;
        }

        ItemHolderRules rules = stack.get(ItematicDataComponentTypes.ITEM_HOLDER_RULES);
        if (rules == null) {
            return null;
        }

        BundleContentsComponent.Builder newBuilder = new BundleContentsComponent.Builder(existingBundleContents);
        newBuilder.itematic$setExtraFields(existingBundleContents, capacity, rules);
        return newBuilder;
    }

    private void add(BundleContentsComponent.Builder bundleContentsBuilder, ItemStack stack, PlayerEntity user) {
        int addedCount = bundleContentsBuilder.add(stack);
        if (addedCount > 0) {
            this.playInsertItemSound(user);
        }
    }

    private void add(BundleContentsComponent.Builder bundleContentsBuilder, Slot slot, PlayerEntity user) {
        int addedCount = bundleContentsBuilder.add(slot, user);
        if (addedCount > 0) {
            this.playInsertItemSound(user);
        }
    }

    private void remove(Entity user, BundleContentsComponent.Builder bundleContentsBuilder, Consumer<ItemStack> onRemoved) {
        ItemStack removedStack = bundleContentsBuilder.removeFirst();
        if (removedStack == null) {
            return;
        }

        user.playSound(this.removeItemSound.value(), 0.8f, 0.8f + user.getWorld().getRandom().nextFloat() * 0.4f);
        onRemoved.accept(removedStack);
    }

    private void removeAndDrop(ItemStack stack, PlayerEntity player) {
        BundleContentsComponent.Builder newBuilder = this.createBuilder(stack);
        if (newBuilder == null) {
            return;
        }

        ItemStack removedStack = newBuilder.removeFirst();
        if (removedStack == null) {
            return;
        }

        player.dropItem(removedStack, true);
        player.playSound(this.emptySound.value(), 0.8f, 0.8f + player.getWorld().getRandom().nextFloat() * 0.4f);
        player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, newBuilder.build());
    }

    private void playInsertItemSound(PlayerEntity user) {
        user.playSound(this.insertItemSound.value(), 0.8f, 0.8f + user.getWorld().getRandom().nextFloat() * 0.4f);
    }
}
