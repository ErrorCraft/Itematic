package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record ItemHolderItemComponent(int capacity, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) implements ItemComponent<ItemHolderItemComponent> {
    public static final Codec<ItemHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("capacity").forGetter(ItemHolderItemComponent::capacity),
        SoundEvent.ENTRY_CODEC.fieldOf("insert_item_sound").forGetter(ItemHolderItemComponent::insertItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("remove_item_sound").forGetter(ItemHolderItemComponent::removeItemSound),
        SoundEvent.ENTRY_CODEC.fieldOf("empty_sound").forGetter(ItemHolderItemComponent::emptySound)
    ).apply(instance, ItemHolderItemComponent::new));
    public static final int NESTED_ITEM_HOLDER_OCCUPANCY = BundleContentsComponentAccessor.nestedItemHolderOccupancy();
    public static final int ITEM_BAR_COLOR = BundleItemAccessor.itemBarColor();

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

        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return false;
        }

        BundleContentsComponent.Builder bundleContentsBuilder = new BundleContentsComponent.Builder(bundleContents);
        bundleContentsBuilder.itematic$setCapacity(this.capacity);
        if (slot.getStack().isEmpty()) {
            this.remove(user, bundleContentsBuilder, removedStack -> this.add(bundleContentsBuilder, slot.insertStack(removedStack), user));
        } else {
            this.add(bundleContentsBuilder, slot, user);
        }

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContentsBuilder.build());
        return true;
    }

    @Override
    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickType clickType, PlayerEntity user, ItemStackConsumer resultStackConsumer) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(user)) {
            return false;
        }

        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return false;
        }

        BundleContentsComponent.Builder bundleContentsBuilder = new BundleContentsComponent.Builder(bundleContents);
        bundleContentsBuilder.itematic$setCapacity(this.capacity);
        if (cursorStack.isEmpty()) {
            this.remove(user, bundleContentsBuilder, resultStackConsumer::set);
        } else {
            this.add(bundleContentsBuilder, cursorStack, user);
        }

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContentsBuilder.build());
        return true;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", (int) bundleContents.itematic$occupancy(), this.capacity).formatted(Formatting.GRAY));
        }
    }

    public boolean itemBarVisible(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return false;
        }
        return bundleContents.itematic$occupancy() > 0.0d;
    }

    public int itemBarStep(ItemStack stack) {
        return Math.min((int)((Item.ITEM_BAR_STEPS - 1) * this.fullness(stack) + 1), Item.ITEM_BAR_STEPS);
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

    public double fullness(ItemStack stack) {
        BundleContentsComponent bundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents == null) {
            return 0.0d;
        }
        return bundleContents.itematic$occupancy() / this.capacity;
    }

    public void onDestroyed(ItemEntity item) {
        BundleContentsComponent bundleContents = item.getStack().get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContents != null) {
            ItemUsage.spawnItemContents(item, bundleContents.stream());
        }
    }

    public static ItemHolderItemComponent of(int capacity, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) {
        return new ItemHolderItemComponent(capacity, insertItemSound, removeItemSound, emptySound);
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
