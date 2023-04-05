package errorcraft.itematic.mixin.client.gui.screen.recipebook;

import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetExtender implements DynamicRegistryManagerAccess {
    @Shadow
    @Final
    private RecipeMatcher recipeFinder;

    @Inject(
        method = "initialize",
        at = @At("TAIL")
    )
    private void initializeAllowRecipeSpecificInitialization(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, AbstractRecipeScreenHandler<?> craftingScreenHandler, CallbackInfo info) {
        this.initializeRecipeSpecific(client.world);
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.recipeFinder.getRegistryManager();
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.recipeFinder.setRegistryManager(registryManager);
    }

    protected void initializeRecipeSpecific(World world) {}
}
