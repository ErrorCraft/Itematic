package net.errorcraft.itematic.item.component.components;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.client.item.BundleTooltipDataAccess;
import net.errorcraft.itematic.inventory.InventoryUtil;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.BundleItemAccessor;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
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
    public static final int ITEM_HOLDER_OCCUPANCY = BundleItemAccessor.itemHolderOccupancy();
    public static final int ITEM_BAR_COLOR = BundleItemAccessor.itemBarColor();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String ITEMS_KEY = BundleItemAccessor.itemsKey();

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
        if (this.dropItems(stack.getOrCreateNbt(), user, world)) {
            user.playSound(this.emptySound.value(), 0.8f, 0.8f + world.getRandom().nextFloat() * 0.4f);
            user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
            return ActionResult.success(world.isClient());
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean clickOnSlot(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity user) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }
        DynamicRegistryManager registryManager = user.getWorld().getRegistryManager();
        ItemStack heldStack = slot.getStack();
        if (heldStack.isEmpty()) {
            this.remove(stack, user, removedStack -> this.add(stack, slot.insertStack(removedStack), user, registryManager));
        } else {
            this.add(stack, heldStack, user, registryManager);
        }
        return true;
    }

    @Override
    public boolean clickedOnWithStack(ItemStack stack, ItemStack cursorStack, Slot slot, ClickType clickType, PlayerEntity user, ItemStackConsumer resultStackConsumer) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(user)) {
            return false;
        }
        if (cursorStack.isEmpty()) {
            this.remove(stack, user, resultStackConsumer::set);
        } else {
            this.add(stack, cursorStack, user, user.getWorld().getRegistryManager());
        }
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (world == null) {
            return;
        }
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", (int) this.getHolderOccupancy(stack, world.getRegistryManager()), this.capacity).formatted(Formatting.GRAY));
    }

    public boolean itemBarVisible(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        return this.getHolderOccupancy(stack, lookup) > 0.0d;
    }

    public int itemBarStep(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        return Math.min((int)((Item.ITEM_BAR_STEPS - 1) * this.fullness(stack, lookup) + 1), Item.ITEM_BAR_STEPS);
    }

    public TooltipData tooltipData(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        DefaultedList<ItemStack> stacks = DefaultedList.copyOf(ItemStack.EMPTY, this.stacks(stack, lookup).toArray(ItemStack[]::new));
        BundleTooltipData data = new BundleTooltipData(stacks, (int) this.getHolderOccupancy(stack, lookup));
        ((BundleTooltipDataAccess) data).itematic$setCapacity(this.capacity);
        return data;
    }

    public double fullness(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        return this.getHolderOccupancy(stack, lookup) / this.capacity;
    }

    public double getHolderOccupancy(ItemStack itemHolderStack, RegistryWrapper.WrapperLookup lookup) {
        return this.stacks(itemHolderStack, lookup)
            .stream()
            .mapToDouble(heldStack -> heldStack.itematic$occupancy(lookup) * heldStack.getCount())
            .sum();
    }

    public void onDestroyed(ItemEntity item) {
        ItemUsage.spawnItemContents(item, this.stacks(item.getStack(), item.getWorld().getRegistryManager()).stream());
    }

    public static ItemHolderItemComponent of(int capacity, RegistryEntry<SoundEvent> insertItemSound, RegistryEntry<SoundEvent> removeItemSound, RegistryEntry<SoundEvent> emptySound) {
        return new ItemHolderItemComponent(capacity, insertItemSound, removeItemSound, emptySound);
    }

    private void add(ItemStack itemHolderStack, ItemStack stackToAdd, Entity user, RegistryWrapper.WrapperLookup lookup) {
        if (stackToAdd.isEmpty() || !stackToAdd.itematic$canBeNested()) {
            return;
        }

        int maxAddCount = this.maxAddCount(itemHolderStack, stackToAdd, lookup);
        if (maxAddCount <= 0) {
            return;
        }

        user.playSound(this.insertItemSound.value(), 0.8f, 0.8f + user.getWorld().getRandom().nextFloat() * 0.4f);
        ItemStack actualStackToAdd = stackToAdd.split(maxAddCount);
        this.add(itemHolderStack, actualStackToAdd, lookup);
    }

    private int maxAddCount(ItemStack itemHolderStack, ItemStack stackToAdd, RegistryWrapper.WrapperLookup lookup) {
        double occupancy = this.getHolderOccupancy(itemHolderStack, lookup);
        double availableOccupancy = this.capacity - occupancy;
        double stackToAddOccupancy = stackToAdd.itematic$occupancy(lookup);
        return (int)(availableOccupancy / stackToAddOccupancy);
    }

    private void add(ItemStack itemHolderStack, ItemStack stackToAdd, RegistryWrapper.WrapperLookup lookup) {
        this.merge(itemHolderStack, stackToAdd, lookup);
        if (stackToAdd.getCount() <= 0) {
            return;
        }

        NbtCompound nbt = itemHolderStack.getOrCreateNbt();
        if (!nbt.contains(ITEMS_KEY)) {
            nbt.put(ITEMS_KEY, new NbtList());
        }

        NbtList stacksNbt = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        tryEncode(stackToAdd, RegistryOps.of(NbtOps.INSTANCE, lookup))
            .ifPresent(stackNbt -> stacksNbt.add(0, stackNbt));
    }

    private void merge(ItemStack itemHolderStack, ItemStack stackToAdd, RegistryWrapper.WrapperLookup lookup) {
        List<ItemStack> heldStacks = this.stacks(itemHolderStack, lookup);
        for (ItemStack heldStack : heldStacks) {
            if (!ItemStack.areItemsAndNbtEqual(heldStack, stackToAdd)) {
                continue;
            }
            int count = Math.min(heldStack.getMaxCount() - heldStack.getCount(), stackToAdd.getCount());
            heldStack.increment(count);
            stackToAdd.decrement(count);
            if (stackToAdd.getCount() <= 0) {
                break;
            }
        }
        this.setStacks(itemHolderStack, heldStacks, lookup);
    }

    private void remove(ItemStack itemHolderStack, Entity user, Consumer<ItemStack> onRemoved) {
        World world = user.getWorld();
        this.removeFirstStack(itemHolderStack, world.getRegistryManager()).ifPresent(removedStack -> {
            user.playSound(this.removeItemSound.value(), 0.8f, 0.8f + world.getRandom().nextFloat() * 0.4f);
            onRemoved.accept(removedStack);
        });
    }

    private boolean dropItems(NbtCompound nbt, PlayerEntity player, World world) {
        if (!nbt.contains(ITEMS_KEY)) {
            return false;
        }
        if (!world.isClient()) {
            DefaultedList<ItemStack> stacks = DefaultedList.of();
            InventoryUtil.readFromNbt(nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE), stacks, world.getRegistryManager());
            for (ItemStack stack : stacks) {
                player.dropItem(stack, true);
            }
        }
        nbt.remove(ITEMS_KEY);
        return true;
    }

    private Optional<ItemStack> removeFirstStack(ItemStack stack, RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(ITEMS_KEY)) {
            return Optional.empty();
        }
        NbtList stacksNbt = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        if (stacksNbt.isEmpty()) {
            return Optional.empty();
        }
        NbtCompound stackNbt = stacksNbt.getCompound(0);
        ItemStack removedStack = ItemStackUtil.fromNbt(stackNbt, lookup);
        stacksNbt.remove(0);
        if (stacksNbt.isEmpty()) {
            stack.removeSubNbt(ITEMS_KEY);
        }
        return Optional.of(removedStack);
    }

    private List<ItemStack> stacks(ItemStack itemHolderStack, RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = itemHolderStack.getOrCreateNbt();
        if (!nbt.contains(ITEMS_KEY)) {
            return List.of();
        }
        List<ItemStack> stacks = new ArrayList<>();
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, lookup);
        for (NbtElement stackNbt : nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE)) {
            tryDecode(stackNbt, ops).ifPresent(stacks::add);
        }
        return stacks;
    }

    private void setStacks(ItemStack stack, List<ItemStack> heldStacks, RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtList stacksNbt = new NbtList();
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, lookup);
        for (ItemStack itemStack : heldStacks) {
            tryEncode(itemStack, ops).ifPresent(stacksNbt::add);
        }
        nbt.put(ITEMS_KEY, stacksNbt);
    }

    private static Optional<ItemStack> tryDecode(NbtElement stackNbt, RegistryOps<NbtElement> ops) {
        return ItemStack.CODEC.parse(ops, stackNbt)
            .resultOrPartial(Util.addPrefix("Failed to decode item holder item: ", LOGGER::error));
    }

    private static Optional<NbtElement> tryEncode(ItemStack stack, RegistryOps<NbtElement> ops) {
        return ItemStack.CODEC.encodeStart(ops, stack)
            .resultOrPartial(Util.addPrefix("Failed to encode item holder item: ", LOGGER::error));
    }
}
