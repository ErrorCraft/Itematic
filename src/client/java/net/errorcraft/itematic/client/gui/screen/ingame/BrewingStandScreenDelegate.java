package net.errorcraft.itematic.client.gui.screen.ingame;

import net.errorcraft.itematic.client.gui.screen.recipebook.BrewingRecipeBookWidget;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.BrewingStandScreenAccessor;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.HandledScreenAccessor;
import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BrewingStandScreenDelegate extends AbstractRecipeBookScreen<BrewingStandMenuDelegate> {
    private final BrewingStandScreen delegate;

    public BrewingStandScreenDelegate(BrewingStandMenuDelegate handler, Inventory inventory, Component title, BrewingStandScreen delegate) {
        super(handler, new BrewingRecipeBookWidget(handler), inventory, title);
        this.delegate = delegate;
    }

    @Override
    protected void init() {
        this.delegate.init(this.width, this.height);
        super.init();
        ((HandledScreenAccessor) this.delegate).itematic$setX(this.leftPos);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 15, this.topPos + 50);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        ((BrewingStandScreenAccessor) this.delegate).itematic$drawBackground(context, delta, mouseX, mouseY);
    }

    @Override
    protected void onRecipeBookButtonClick() {
        ((HandledScreenAccessor) this.delegate).itematic$setX(this.leftPos);
    }
}
