package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.client.gui.screen.recipebook.RecipeBookWidgetAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(AbstractFurnaceRecipeBookWidget.class)
public abstract class AbstractFurnaceRecipeBookScreenExtender extends RecipeBookWidget<AbstractFurnaceScreenHandler> implements RecipeBookWidgetAccess {
    @Shadow
    @Nullable
    private List<ItemStack> fuels;

    public AbstractFurnaceRecipeBookScreenExtender(AbstractFurnaceScreenHandler craftingScreenHandler) {
        super(craftingScreenHandler);
    }

    @Override
    public void itematic$initializeRecipeSpecific(World world) {
        this.fuels = world.itematic$getItemAccess()
            .streamEntries()
            .filter(reference -> reference.value().itematic$hasBehavior(ItemComponentTypes.FUEL))
            .map(ItemStack::new)
            .toList();
    }
}
