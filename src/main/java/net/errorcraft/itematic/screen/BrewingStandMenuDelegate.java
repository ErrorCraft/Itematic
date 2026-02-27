package net.errorcraft.itematic.screen;

import net.errorcraft.itematic.mixin.screen.BrewingStandScreenHandlerAccessor;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.input.BrewingRecipeInput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.OptionalInt;

public class BrewingStandMenuDelegate extends AbstractRecipeScreenHandler<BrewingRecipeInput, BrewingRecipe<?>> {
    public static final int FIRST_INPUT_SLOT = BrewingStandScreenHandlerAccessor.inputSlotStart();
    public static final int LAST_INPUT_SLOT = BrewingStandScreenHandlerAccessor.inputSlotEnd();
    public static final int INGREDIENT_SLOT = BrewingStandScreenHandlerAccessor.ingredientSlot();

    private final BrewingStandScreenHandler delegate;
    private final Inventory inventory;
    private final World world;

    public BrewingStandMenuDelegate(int syncId, PlayerInventory inventory) {
        this(new BrewingStandScreenHandler(syncId, inventory), inventory.player.getWorld());
    }

    public BrewingStandMenuDelegate(BrewingStandScreenHandler delegate, World world) {
        super(delegate.getType(), delegate.syncId);
        this.delegate = delegate;
        this.inventory = ((BrewingStandScreenHandlerAccessor) delegate).itematic$inventory();
        this.world = world;
        delegate.slots.forEach(this::addSlot);
    }

    public BrewingStandScreenHandler delegate() {
        return this.delegate;
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher matcher) {
        if (this.inventory instanceof RecipeInputProvider recipeInputProvider) {
            recipeInputProvider.provideRecipeInputs(matcher);
        }
    }

    @Override
    public void clearCraftingSlots() {
        for (int i = FIRST_INPUT_SLOT; i < LAST_INPUT_SLOT; i++) {
            this.slots.get(i).setStackNoCallbacks(ItemStack.EMPTY);
        }

        this.slots.get(INGREDIENT_SLOT).setStackNoCallbacks(ItemStack.EMPTY);
    }

    @Override
    public boolean matches(RecipeEntry<BrewingRecipe<?>> recipe) {
        BrewingRecipeInput input = new BrewingRecipeInput(
            BrewingStandMenuDelegate.this.inventory.getStack(FIRST_INPUT_SLOT),
            BrewingStandMenuDelegate.this.inventory.getStack(INGREDIENT_SLOT)
        );
        return recipe.value().matches(input, this.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return FIRST_INPUT_SLOT;
    }

    @Override
    public int getCraftingWidth() {
        return 2;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 4;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return ItematicRecipeBookCategories.BREWING;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return (index >= FIRST_INPUT_SLOT && index <= LAST_INPUT_SLOT) || index == INGREDIENT_SLOT;
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
    public void setPreviousTrackedSlot(int slot, ItemStack stack) {
        this.delegate.setPreviousTrackedSlot(slot, stack);
    }

    @Override
    public void setPreviousTrackedSlotMutable(int slot, ItemStack stack) {
        this.delegate.setPreviousTrackedSlotMutable(slot, stack);
    }

    @Override
    public void setPreviousCursorStack(ItemStack stack) {
        this.delegate.setPreviousCursorStack(stack);
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
}
