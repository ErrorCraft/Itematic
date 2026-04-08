package net.errorcraft.itematic.screen;

import net.errorcraft.itematic.mixin.screen.BrewingStandScreenHandlerAccessor;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookTypes;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.OptionalInt;

public class BrewingStandMenuDelegate extends AbstractRecipeScreenHandler {
    public static final int FIRST_INPUT_SLOT = BrewingStandScreenHandlerAccessor.inputSlotStart();
    public static final int INGREDIENT_SLOT = BrewingStandScreenHandlerAccessor.ingredientSlot();

    private final BrewingStandScreenHandler delegate;
    private final Inventory inventory;

    public BrewingStandMenuDelegate(int syncId, PlayerInventory inventory) {
        this(new BrewingStandScreenHandler(syncId, inventory));
    }

    public BrewingStandMenuDelegate(BrewingStandScreenHandler delegate) {
        super(delegate.getType(), delegate.syncId);
        this.delegate = delegate;
        this.inventory = ((BrewingStandScreenHandlerAccessor) delegate).itematic$inventory();
        delegate.slots.forEach(this::addSlot);
    }

    public BrewingStandScreenHandler delegate() {
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
    public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
        final List<Slot> slots = List.of(
            this.getSlot(FIRST_INPUT_SLOT),
            this.getSlot(INGREDIENT_SLOT)
        );
        return InputSlotFiller.fill(
            new BrewingRecipeHandler(slots, world),
            1,
            2,
            List.of(
                this.getSlot(FIRST_INPUT_SLOT),
                this.getSlot(INGREDIENT_SLOT)
            ),
            slots,
            inventory,
            (RecipeEntry<BrewingRecipe<?>>) recipe,
            craftAll,
            creative
        );
    }

    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        if (this.inventory instanceof RecipeInputProvider recipeInputProvider) {
            recipeInputProvider.provideRecipeInputs(finder);
        }
    }

    @Override
    public RecipeBookType getCategory() {
        return ItematicRecipeBookTypes.BREWING;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.delegate.canUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return this.delegate.quickMove(player, slot);
    }

    @Override
    public void addListener(ScreenHandlerListener listener) {
        this.delegate.addListener(listener);
    }

    @Override
    public void updateSyncHandler(ScreenHandlerSyncHandler handler) {
        this.delegate.updateSyncHandler(handler);
    }

    @Override
    public void syncState() {
        this.delegate.syncState();
    }

    @Override
    public void removeListener(ScreenHandlerListener listener) {
        this.delegate.removeListener(listener);
    }

    @Override
    public DefaultedList<ItemStack> getStacks() {
        return this.delegate.getStacks();
    }

    @Override
    public void sendContentUpdates() {
        this.delegate.sendContentUpdates();
    }

    @Override
    public void updateToClient() {
        this.delegate.updateToClient();
    }

    @Override
    public void setReceivedStack(int slot, ItemStack stack) {
        this.delegate.setReceivedStack(slot, stack);
    }

    @Override
    public void setReceivedHash(int slot, ItemStackHash hash) {
        this.delegate.setReceivedHash(slot, hash);
    }

    @Override
    public void setReceivedCursorHash(ItemStackHash cursorStackHash) {
        this.delegate.setReceivedCursorHash(cursorStackHash);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        return this.delegate.onButtonClick(player, id);
    }

    @Override
    public Slot getSlot(int index) {
        return this.delegate.getSlot(index);
    }

    @Override
    public void selectBundleStack(int slot, int selectedStack) {
        this.delegate.selectBundleStack(slot, selectedStack);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        this.delegate.onSlotClick(slotIndex, button, actionType, player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        this.delegate.onClosed(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.delegate.onContentChanged(inventory);
    }

    @Override
    public void setStackInSlot(int slot, int revision, ItemStack stack) {
        this.delegate.setStackInSlot(slot, revision, stack);
    }

    @Override
    public void updateSlotStacks(int revision, List<ItemStack> stacks, ItemStack cursorStack) {
        this.delegate.updateSlotStacks(revision, stacks, cursorStack);
    }

    @Override
    public void setProperty(int id, int value) {
        this.delegate.setProperty(id, value);
    }

    @Override
    public void setCursorStack(ItemStack stack) {
        this.delegate.setCursorStack(stack);
    }

    @Override
    public ItemStack getCursorStack() {
        return this.delegate.getCursorStack();
    }

    @Override
    public void disableSyncing() {
        this.delegate.disableSyncing();
    }

    @Override
    public void enableSyncing() {
        this.delegate.enableSyncing();
    }

    @Override
    public void copySharedSlots(ScreenHandler handler) {
        this.delegate.copySharedSlots(handler);
    }

    @Override
    public OptionalInt getSlotIndex(Inventory inventory, int index) {
        return this.delegate.getSlotIndex(inventory, index);
    }

    @Override
    public int getRevision() {
        return this.delegate.getRevision();
    }

    @Override
    public int nextRevision() {
        return this.delegate.nextRevision();
    }

    private class BrewingRecipeHandler implements InputSlotFiller.Handler<BrewingRecipe<?>> {
        private final List<Slot> slots;
        private final ServerWorld world;

        private BrewingRecipeHandler(List<Slot> slots, ServerWorld world) {
            this.slots = slots;
            this.world = world;
        }

        @Override
        public void populateRecipeFinder(RecipeFinder finder) {
            BrewingStandMenuDelegate.this.populateRecipeFinder(finder);
        }

        @Override
        public void clear() {
            this.slots.forEach(slot -> slot.setStackNoCallbacks(ItemStack.EMPTY));
        }

        @Override
        public boolean matches(RecipeEntry<BrewingRecipe<?>> entry) {
            BrewingRecipeInput input = new BrewingRecipeInput(
                BrewingStandMenuDelegate.this.inventory.getStack(FIRST_INPUT_SLOT),
                BrewingStandMenuDelegate.this.inventory.getStack(INGREDIENT_SLOT)
            );
            return entry.value().matches(input, this.world);
        }
    }
}
