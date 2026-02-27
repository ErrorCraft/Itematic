package net.errorcraft.itematic.client.gui.screen.ingame;

import net.errorcraft.itematic.client.gui.screen.recipebook.BrewingRecipeBookWidget;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.BrewingStandScreenAccessor;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.HandledScreenAccessor;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class BrewingStandScreenDelegate extends HandledScreen<BrewingStandMenuDelegate> implements RecipeBookProvider {
    private final BrewingStandScreen delegate;
    private final RecipeBookWidget recipeBook = new BrewingRecipeBookWidget();
    private boolean narrow;

    public BrewingStandScreenDelegate(BrewingStandMenuDelegate handler, PlayerInventory inventory, Text title, BrewingStandScreen delegate) {
        super(handler, inventory, title);
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void init() {
        this.delegate.init(this.client, this.width, this.height);
        super.init();
        this.narrow = this.width < 379;
        this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        ((HandledScreenAccessor) this.delegate).itematic$setX(this.x);
        this.addDrawableChild(new TexturedButtonWidget(this.x + 15, this.y + 50, 20, 18, RecipeBookWidget.BUTTON_TEXTURES, button -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            ((HandledScreenAccessor) this.delegate).itematic$setX(this.x);
            button.setPosition(this.x + 15, this.y + 50);
        }));
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.recipeBook.update();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.recipeBook.isOpen() && this.narrow) {
            this.renderBackground(context, mouseX, mouseY, delta);
            this.recipeBook.render(context, mouseX, mouseY, delta);
        } else {
            super.render(context, mouseX, mouseY, delta);
            this.recipeBook.render(context, mouseX, mouseY, delta);
            this.recipeBook.drawGhostSlots(context, this.x, this.y, false, delta);
        }

        this.drawMouseoverTooltip(context, mouseX, mouseY);
        this.recipeBook.drawTooltip(context, this.x, this.y, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        ((BrewingStandScreenAccessor) this.delegate).itematic$drawBackground(context, delta, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return this.narrow && this.recipeBook.isOpen() || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
        this.recipeBook.slotClicked(slot);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.recipeBook.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean clickedOutside = mouseX < left || mouseY < top || mouseX >= left + this.backgroundWidth || mouseY >= top + this.backgroundHeight;
        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && clickedOutside;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.recipeBook.charTyped(chr, modifiers) || super.charTyped(chr, modifiers);
    }

    @Override
    public void refreshRecipeBook() {
        this.recipeBook.refresh();
    }

    @Override
    public RecipeBookWidget getRecipeBookWidget() {
        return this.recipeBook;
    }
}
