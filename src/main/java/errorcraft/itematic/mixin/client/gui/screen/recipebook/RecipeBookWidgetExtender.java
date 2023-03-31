package errorcraft.itematic.mixin.client.gui.screen.recipebook;

import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetExtender implements DynamicRegistryManagerAccess {
    @Shadow
    @Final
    private RecipeMatcher recipeFinder;

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.recipeFinder.getRegistryManager();
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.recipeFinder.setRegistryManager(registryManager);
    }
}
