package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.component.type.BundleContentsComponentUtil;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.errorcraft.itematic.util.Util;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderItemComponent(int capacity, ItemHolderRules rules, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) implements ItemComponent<ItemHolderItemComponent> {
    public static final Codec<ItemHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("capacity").forGetter(ItemHolderItemComponent::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemComponent::rules),
        SoundEvent.ENTRY_CODEC.fieldOf("insert_item_sound").forGetter(ItemHolderItemComponent::insertItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("remove_item_sound").forGetter(ItemHolderItemComponent::removeItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("empty_sound").forGetter(ItemHolderItemComponent::emptySound)
    ).apply(instance, ItemHolderItemComponent::new));
    public static final int ITEM_BAR_COLOR = BundleItemAccessor.itemBarColor();

    public static ItemHolderItemComponent of(int capacity, ItemHolderRules rules, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) {
        return new ItemHolderItemComponent(capacity, rules, insertItemSound, removeItemSound, emptySound);
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
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!BundleItemAccessor.dropAllBundledItems(stack, user)) {
            return ActionResult.PASS;
        }
        user.playSound(this.emptySound.value(), 0.8f, 0.8f + world.getRandom().nextFloat() * 0.4f);
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ActionResult.success(world.isClient());
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
        builder.add(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponentUtil.create(this.rules));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            int occupancy = Util.multiplyFraction(bundleContents.getOccupancy(), Item.DEFAULT_MAX_COUNT);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", occupancy, this.capacity).formatted(Formatting.GRAY));
        }
    }

    public boolean itemBarVisible(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return false;
        }
        return bundleContents.getOccupancy().compareTo(Fraction.ZERO) > 0 ;
    }

    public int itemBarStep(ItemStack stack) {
        int step = this.fullness(stack)
            .multiplyBy(Fraction.getFraction((Item.ITEM_BAR_STEPS - 1) * Item.DEFAULT_MAX_COUNT, 1))
            .intValue();
        return Math.min(step, Item.ITEM_BAR_STEPS);
    }

    public Optional<TooltipData> tooltipData(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return Optional.empty();
        }
        BundleTooltipData data = new BundleTooltipData(bundleContents);
        data.itematic$setCapacity(this.capacity);
        return Optional.of(data);
    }

    public Fraction fullness(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return Fraction.ZERO;
        }
        return bundleContents.getOccupancy().multiplyBy(Fraction.getFraction(1, this.capacity));
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
        BundleContentsComponent.Builder newBuilder = new BundleContentsComponent.Builder(existingBundleContents);
        newBuilder.itematic$setCapacity(this.capacity);
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
        World world = user.getWorld();
        ItemStack removedStack = bundleContentsBuilder.removeFirst();
        if (removedStack == null) {
            return;
        }
        user.playSound(this.removeItemSound.value(), 0.8f, 0.8f + world.getRandom().nextFloat() * 0.4f);
        onRemoved.accept(removedStack);
    }

    private void playInsertItemSound(PlayerEntity user) {
        user.playSound(this.insertItemSound.value(), 0.8f, 0.8f + user.getWorld().getRandom().nextFloat() * 0.4f);
    }
}
