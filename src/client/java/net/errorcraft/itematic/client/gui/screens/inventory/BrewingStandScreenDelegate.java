package net.errorcraft.itematic.client.gui.screens.inventory;

import net.errorcraft.itematic.client.gui.screens.recipebook.BrewingRecipeBookComponent;
import net.errorcraft.itematic.mixin.client.gui.screens.inventory.AbstractContainerScreenAccessor;
import net.errorcraft.itematic.mixin.client.gui.screens.inventory.BrewingStandScreenAccessor;
import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BrewingStandScreenDelegate extends AbstractRecipeBookScreen<BrewingStandMenuDelegate> {
    private final BrewingStandScreen delegate;

    public BrewingStandScreenDelegate(BrewingStandMenuDelegate menu, Inventory inventory, Component title, BrewingStandScreen delegate) {
        super(menu, new BrewingRecipeBookComponent(menu), inventory, title);
        this.delegate = delegate;
    }

    @Override
    protected void init() {
        this.delegate.init(this.width, this.height);
        super.init();
        ((AbstractContainerScreenAccessor) this.delegate).itematic$leftPos(this.leftPos);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 15, this.topPos + 50);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        ((BrewingStandScreenAccessor) this.delegate).itematic$extractBackground(graphics, mouseX, mouseY, a);
    }

    @Override
    protected void onRecipeBookButtonClick() {
        ((AbstractContainerScreenAccessor) this.delegate).itematic$leftPos(this.leftPos);
    }
}
