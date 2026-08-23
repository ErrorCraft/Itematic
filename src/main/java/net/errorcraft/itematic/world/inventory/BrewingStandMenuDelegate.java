package net.errorcraft.itematic.world.inventory;

import net.errorcraft.itematic.mixin.world.inventory.BrewingStandMenuAccessor;
import net.errorcraft.itematic.world.item.crafting.BrewingRecipe;
import net.errorcraft.itematic.world.item.crafting.BrewingRecipeInput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.HashedStack;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.OptionalInt;

public class BrewingStandMenuDelegate extends RecipeBookMenu {
    public static final int FIRST_INPUT_SLOT = BrewingStandMenuAccessor.inputSlotStart();
    public static final int INGREDIENT_SLOT = BrewingStandMenuAccessor.ingredientSlot();

    private final BrewingStandMenu delegate;
    private final Container inventory;

    public BrewingStandMenuDelegate(int syncId, Inventory inventory) {
        this(new BrewingStandMenu(syncId, inventory));
    }

    public BrewingStandMenuDelegate(BrewingStandMenu delegate) {
        super(delegate.getType(), delegate.containerId);
        this.delegate = delegate;
        this.inventory = ((BrewingStandMenuAccessor) delegate).itematic$inventory();
        delegate.slots.forEach(this::addSlot);
    }

    public BrewingStandMenu delegate() {
        return this.delegate;
    }

    public Slot firstInputSlot() {
        return this.slots.get(FIRST_INPUT_SLOT);
    }

    public Slot ingredientSlot() {
        return this.slots.get(INGREDIENT_SLOT);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PostPlaceAction handlePlacement(boolean craftAll, boolean creative, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
        final List<Slot> slots = List.of(
            this.getSlot(FIRST_INPUT_SLOT),
            this.getSlot(INGREDIENT_SLOT)
        );
        return ServerPlaceRecipe.placeRecipe(
            new BrewingRecipeCraftingMenuAccess(slots, level),
            1,
            2,
            List.of(
                this.getSlot(FIRST_INPUT_SLOT),
                this.getSlot(INGREDIENT_SLOT)
            ),
            slots,
            inventory,
            (RecipeHolder<BrewingRecipe<?>>) recipe,
            craftAll,
            creative
        );
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents finder) {
        if (this.inventory instanceof StackedContentsCompatible recipeInputProvider) {
            recipeInputProvider.fillStackedContents(finder);
        }
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.ITEMATIC_BREWING;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.delegate.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return this.delegate.quickMoveStack(player, slot);
    }

    @Override
    public void addSlotListener(ContainerListener listener) {
        this.delegate.addSlotListener(listener);
    }

    @Override
    public void setSynchronizer(ContainerSynchronizer handler) {
        this.delegate.setSynchronizer(handler);
    }

    @Override
    public void sendAllDataToRemote() {
        this.delegate.sendAllDataToRemote();
    }

    @Override
    public void removeSlotListener(ContainerListener listener) {
        this.delegate.removeSlotListener(listener);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.delegate.getItems();
    }

    @Override
    public void broadcastChanges() {
        this.delegate.broadcastChanges();
    }

    @Override
    public void broadcastFullState() {
        this.delegate.broadcastFullState();
    }

    @Override
    public void setRemoteSlot(int slot, ItemStack stack) {
        this.delegate.setRemoteSlot(slot, stack);
    }

    @Override
    public void setRemoteSlotUnsafe(int slot, HashedStack hash) {
        this.delegate.setRemoteSlotUnsafe(slot, hash);
    }

    @Override
    public void setRemoteCarried(HashedStack cursorStackHash) {
        this.delegate.setRemoteCarried(cursorStackHash);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return this.delegate.clickMenuButton(player, id);
    }

    @Override
    public Slot getSlot(int index) {
        return this.delegate.getSlot(index);
    }

    @Override
    public void setSelectedBundleItemIndex(int slot, int selectedStack) {
        this.delegate.setSelectedBundleItemIndex(slot, selectedStack);
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        super.clicked(slotIndex, buttonNum, containerInput, player);
    }

    @Override
    public void removed(Player player) {
        this.delegate.removed(player);
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.delegate.slotsChanged(inventory);
    }

    @Override
    public void setItem(int slot, int revision, ItemStack stack) {
        this.delegate.setItem(slot, revision, stack);
    }

    @Override
    public void initializeContents(int revision, List<ItemStack> stacks, ItemStack cursorStack) {
        this.delegate.initializeContents(revision, stacks, cursorStack);
    }

    @Override
    public void setData(int id, int value) {
        this.delegate.setData(id, value);
    }

    @Override
    public void setCarried(ItemStack stack) {
        this.delegate.setCarried(stack);
    }

    @Override
    public ItemStack getCarried() {
        return this.delegate.getCarried();
    }

    @Override
    public void suppressRemoteUpdates() {
        this.delegate.suppressRemoteUpdates();
    }

    @Override
    public void resumeRemoteUpdates() {
        this.delegate.resumeRemoteUpdates();
    }

    @Override
    public void transferState(AbstractContainerMenu handler) {
        this.delegate.transferState(handler);
    }

    @Override
    public OptionalInt findSlot(Container inventory, int index) {
        return this.delegate.findSlot(inventory, index);
    }

    @Override
    public int getStateId() {
        return this.delegate.getStateId();
    }

    @Override
    public int incrementStateId() {
        return this.delegate.incrementStateId();
    }

    private class BrewingRecipeCraftingMenuAccess implements ServerPlaceRecipe.CraftingMenuAccess<BrewingRecipe<?>> {
        private final List<Slot> slots;
        private final ServerLevel level;

        private BrewingRecipeCraftingMenuAccess(List<Slot> slots, ServerLevel level) {
            this.slots = slots;
            this.level = level;
        }

        @Override
        public void fillCraftSlotsStackedContents(StackedItemContents finder) {
            BrewingStandMenuDelegate.this.fillCraftSlotsStackedContents(finder);
        }

        @Override
        public void clearCraftingContent() {
            this.slots.forEach(slot -> slot.set(ItemStack.EMPTY));
        }

        @Override
        public boolean recipeMatches(RecipeHolder<BrewingRecipe<?>> entry) {
            BrewingRecipeInput input = new BrewingRecipeInput(
                BrewingStandMenuDelegate.this.inventory.getItem(FIRST_INPUT_SLOT),
                BrewingStandMenuDelegate.this.inventory.getItem(INGREDIENT_SLOT)
            );
            return entry.value().matches(input, this.level);
        }
    }
}
