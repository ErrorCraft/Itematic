package net.errorcraft.itematic.client.gui.screen.ingame;

import net.errorcraft.itematic.client.gui.screen.recipebook.BrewingRecipeBookWidget;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.BrewingStandScreenAccessor;
import net.errorcraft.itematic.mixin.client.gui.screen.ingame.HandledScreenAccessor;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BrewingStandScreenDelegate extends RecipeBookScreen<BrewingStandMenuDelegate> {
    private final BrewingStandScreen delegate;

    public BrewingStandScreenDelegate(BrewingStandMenuDelegate handler, PlayerInventory inventory, Text title, BrewingStandScreen delegate) {
        super(handler, new BrewingRecipeBookWidget(handler), inventory, title);
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void init() {
        this.delegate.init(this.client, this.width, this.height);
        super.init();
        ((HandledScreenAccessor) this.delegate).itematic$setX(this.x);
    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {
        return new ScreenPos(this.x + 15, this.y + 50);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        ((BrewingStandScreenAccessor) this.delegate).itematic$drawBackground(context, delta, mouseX, mouseY);
    }

    @Override
    protected void onRecipeBookToggled() {
        ((HandledScreenAccessor) this.delegate).itematic$setX(this.x);
    }
}
