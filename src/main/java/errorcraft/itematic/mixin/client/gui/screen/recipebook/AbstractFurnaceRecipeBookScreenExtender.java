package errorcraft.itematic.mixin.client.gui.screen.recipebook;

import errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(AbstractFurnaceRecipeBookScreen.class)
public class AbstractFurnaceRecipeBookScreenExtender extends RecipeBookWidget {
    @Shadow
    @Nullable
    private Ingredient fuels;

    protected void initializeRecipeSpecific(World world) {
        this.fuels = Ingredient.ofStacks(world.getItemAccess()
            .streamEntries()
            .filter(reference -> reference.value().hasComponent(ItemComponentTypes.FUEL))
            .map(ItemStack::new)
        );
    }
}
